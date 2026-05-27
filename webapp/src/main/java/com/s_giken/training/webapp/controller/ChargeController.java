package com.s_giken.training.webapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.s_giken.training.webapp.exception.NotFoundException;
import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;
import com.s_giken.training.webapp.service.IChargeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



/**
 * 料金表管理のコントローラークラス
 */
@Controller
@RequestMapping("/charge")
public class ChargeController {
    private final IChargeService service;

    /**
     * 料金表のコントローラークラスのコンストラクタ
     * @param service　料金表のサービスクラスをDI
    */
    public ChargeController(IChargeService service){
        this.service = service;
    }
    
    /**
     * 料金表検索画面の表示
     * @param charge　Tymeleafに渡すデータ
     * @return　料金表検索画面のHTML
     */
    @GetMapping("/search")
    public String chargeSearch(Model model) {
        var Charge = new ChargeSearchCondition();
        model.addAttribute("charge", Charge);
        return "charge-search-condition";
    }

    /**
     * 料金表検索結果画面の表示
     * @param charge　料金表検索で入力された条件
     * @param model　Tymeleafに渡すデータ
     * @return　料金表検索結果画面のHTML
     */
    @PostMapping("/search")
    public String postMethodName(@ModelAttribute ("charge") ChargeSearchCondition charge, 
        Model model) {
        var list = service.findByNameLike(charge);
        model.addAttribute("list", list);
        return "charge-search-result";
    }

    /**
     * 料金表編集画面の表示
     * @param charge　料金表検索で入力された条件
     * @param model　Tymeleafに渡すデータ
     * @return　料金表検索結果画面のHTML
     */
    @GetMapping("/edit/{id}")
    public String editCharge(@PathVariable Long id, Model model) {
        var editId = service.findById(id);
        if (!editId.isPresent()){
            throw new NotFoundException(String.format("指定したchargeId(%d)の料金情報が存在しません", id));
        }
        model.addAttribute("isAddMode", false);
        model.addAttribute("charge", editId.get());
        return "charge-edit";
    }

    /**
     * 新規追加画面
     * @param model　thymeleafに渡すデータ
     * @return　編集画面のHTML
     */
    @GetMapping("/add")
    public String formAddCharge(Model model) {
        var charge = new Charge();
        model.addAttribute("isAddMode", true);
        model.addAttribute("charge", charge);
        return "charge-edit";
    }
    
    /**
     * 料金情報を登録する
     * @param charge　入力された値
     * @param result　エラー文
     * @param redirectAttributes　保存メッセージ
     * @return　該当のchargeId
     */
    @PostMapping("/add")
    @Transactional
    public String addCharge(@Validated Charge charge, BindingResult result, 
        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "charge-edit";
        }
        service.add(charge);
        redirectAttributes.addFlashAttribute("message", "保存しました");
        return "redirect:/charge/edit/" + charge.getChargeId();
    }
    
    /**
     * 料金情報を更新する
     * @param charge　入力された値
     * @param result　エラー文
     * @param redirectAttributes　保存メッセージ
     * @return　該当のchargeId
     */
    @PostMapping("/update")
    @Transactional
    public String chargeUpdate(@Validated Charge charge, BindingResult result, 
        RedirectAttributes redirectAttributes) {
         if (result.hasErrors()) {
            return "charge-edit";
        }
        service.update(charge);
        redirectAttributes.addFlashAttribute("message", "保存しました");
        return "redirect:/charge/edit/" + charge.getChargeId();
    }
    
    /**
     * 料金情報を削除する
     * @param id　削除したいchargeId
     * @param redirectAttributes　削除完了メッセージ
     * @return　検索画面へ遷移
     */
    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteCharge(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var deleteId = service.findById(id);
        if (!deleteId.isPresent()){
            throw new NotFoundException(String.format("指定したchargeId(%d)の料金情報は存在しません"));
        }
        service.deleteId(id);
        redirectAttributes.addFlashAttribute("message", "削除しました");
        return "redirect:/charge/search";
    }   
}
