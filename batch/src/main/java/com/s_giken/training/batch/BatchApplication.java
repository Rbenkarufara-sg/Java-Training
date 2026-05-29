package com.s_giken.training.batch;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class BatchApplication implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(BatchApplication.class);
	private final JdbcTemplate jdbcTemplate;

	/**
	 * SpringBoot エントリポイント
	 * 
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param jdbcTemplate SpringBootから注入される JdbcTemplate オブジェクト
	 */
	public BatchApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * コマンドラインプログラムのエントリ―ポイント
	 * 
	 * @param args コマンドライン引数
	 */
	@Override
	@Transactional
	public void run(String... args) throws RuntimeException {
		logger.info("-".repeat(40));

		// TODO: ここにバッチ処理のコードを記述する
		try {
			if (args.length != 1) {
				logger.error("対象年月を指定してください");
				return;
			}
			var formatter = DateTimeFormatter.ofPattern("yyyyMM");
			var ym = YearMonth.parse(args[0], formatter);
			LocalDate targetDate = ym.atDay(1);
			LocalDate lastDay = targetDate.plusMonths(1).minusDays(1);
			logger.info("対象年月:{}",targetDate);

			// - データベースからデータを取得する
			String sql = "select count(*) from T_BILLING_STATUS where billing_ym = ? and is_commit = true";
			Integer count = jdbcTemplate.queryForObject(sql, Integer.class, targetDate);

			if (count != null && count > 0){
				logger.info("請求データは確定済みです");
				return;
			}

			String memberSql = "select * from T_MEMBER where start_date <= ? and (end_date is null or end_date >= ?)";
			List<Map<String, Object>> members = jdbcTemplate.queryForList(memberSql, lastDay, targetDate);
			
			String chargeSql = "select * from T_CHARGE where start_date <= ? and (end_date is null or end_date >= ?)";
			List<Map<String, Object>> charges = jdbcTemplate.queryForList(chargeSql, lastDay, targetDate);

			// - データを加工する
			jdbcTemplate.update("delete from T_BILLING_DETAIL_DATA where billing_ym = ?", targetDate);
			jdbcTemplate.update("delete from T_BILLING_DATA where billing_ym = ?", targetDate);
			jdbcTemplate.update("delete from T_BILLING_STATUS where billing_ym = ?", targetDate);

			jdbcTemplate.update("insert into T_BILLING_STATUS (billing_ym, is_commit) values (?, false)", targetDate);

			// - 加工したデータをデータベースに登録する
			for (Map<String, Object> member : members){
				long totalAmount = 0;

				for (Map<String, Object> charge : charges){
					totalAmount += ((Number) charge.get("amount")).longValue();
				}

				double taxRatio = 0.1;
				long totalWithTax = (long) (totalAmount * (1 + taxRatio));
				
				jdbcTemplate.update("insert into T_BILLING_DATA " +
					" (billing_ym, member_id, mail, name, address, start_date, end_date, payment_method, " +
					" amount, tax_ratio, total, created_at, modified_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?, 0.1, ?, " +
					" current_timestamp, current_timestamp)", 
					targetDate, member.get("member_id"), member.get("mail"), member.get("name"), 
					member.get("address"), member.get("start_date"), member.get("end_date"), 
					member.get("payment_method"), totalAmount, totalWithTax);

				for (Map<String, Object> charge : charges){
					jdbcTemplate.update("insert into T_BILLING_DETAIL_DATA " +
						" (billing_ym, member_id, charge_id, name, amount, start_date, end_date, created_at, modified_at) " +
						" values (?, ?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp) ",
						targetDate, member.get("member_id"), charge.get("charge_id"), charge.get("name"), 
						charge.get("amount"),charge.get("start_date"), charge.get("end_date"));
				}
			}

		} catch (Exception e){
			logger.error("請求データ作成処理中にエラーが発生しました　メッセージ:{}", e.getMessage(), e);
			throw new RuntimeException("バッチ処理が失敗しました", e);
		}
		
		logger.info("-".repeat(40));
	}
}
