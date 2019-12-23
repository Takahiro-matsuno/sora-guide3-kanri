package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.taxi

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.SignUpForm
import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TaxiCompanyForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.AirportCompanyService
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.TaxiService
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TaxiInformationEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.TaxiCompanyRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/taxi")
class TaxiController(
        private val airportCompanyService: AirportCompanyService,
        private val taxiCompanyRepository: TaxiCompanyRepository,
        private val taxiService: TaxiService
) {

    /**
     * タクシー会社一覧画面
     */
    @GetMapping("/list")
    fun getTaxiList(
            @AuthenticationPrincipal user: User,
            model: Model
    ): String {
        //ユーザーの空港会社
        var airportCompanyId = user.getCompanyId()

        //空港とタクシー会社のひも付きテーブルを検索し、タクシー会社IDのリストを取得
        val companyIdList = airportCompanyService.getCompanyList(airportCompanyId)
        var taxiCompanyList = ArrayList<TaxiInformationEntity>()

        //タクシー会社IDのリストからタクシー会社の情報を取得
        if (companyIdList.any()) {
            //タクシー会社IDのリストが取得できた場合のみ
            for (companyId in companyIdList) {
                val company = taxiCompanyRepository.findById(companyId.taxiId)

                if (company.isPresent) {
                    taxiCompanyList.add(company.get())
                }
            }
        } else {
            model.addAttribute("errorMessage", "タクシー情報がありません")
        }
        model.addAttribute("taxiCompanyList", taxiCompanyList)

        //画面を表示
        return "taxi/taxi-list"
    }

    /**
     * タクシー会社追加画面
     */
    @GetMapping("/add")
    fun getTaxiAdd(
            @ModelAttribute taxiCompanyForm: TaxiCompanyForm,
            model: Model
    ): String {

        //model.addAttribute("taxiCompanyForm",TaxiCompanyForm())
        return "taxi/taxi-add"
    }


    /**
     * タクシー会社追加処理
     */
    @PostMapping("/add")
    fun postTaxiAdd(
            @AuthenticationPrincipal user: User,
            @ModelAttribute @Validated taxiCompanyForm: TaxiCompanyForm,
            bindingResult: BindingResult,
            model: Model
    ): String {

        if (bindingResult.hasErrors()) {
            //TODO バインドでエラーのとき、エラーメッセージを表示させる
            return getTaxiAdd(taxiCompanyForm, model)

        } else {
            //タクシー会社登録処理(taxi_infoに追加する処理)
            val success = taxiService.registerTaxi(taxiCompanyForm,user.getCompanyId())

            if (success) {
                //TODO テキストボックスをクリアしておきたい
                model.addAttribute("message", "タクシー会社を追加しました。")
            }
        }
        return "taxi/taxi-add"
    }

    /**
     * タクシー会社詳細画面
     */
    @GetMapping("/detail")
    fun getTaxiDelete(
        @RequestParam("id") id: String,
        model: Model
    ): String {

        var taxiCompany = taxiCompanyRepository.findById(id)

        var taxiInfo = taxiCompany.get()

        model.addAttribute("taxiInfo",taxiInfo)

        return "/taxi/taxi-detail"
    }

    /**
     * タクシー会社削除処理
     */
    @PostMapping("/delete")
    fun postTaxiDelete(
            @AuthenticationPrincipal user: User,
            taxiInfo: TaxiInformationEntity,
            model: Model
    ): String {

        //TODO ユーザーの空港に紐づくタクシー会社かチェックする

        taxiService.deleteTaxi(taxiInfo.companyId)
        return "redirect:/taxi/list"
    }

    /**
     * タクシー会社更新処理
     */
    @PostMapping("/update")
    fun postTaxiUpdate(
            @AuthenticationPrincipal user: User,
            taxiInfo: TaxiInformationEntity,
            model: Model
    ): String {

        //TODO ユーザーの空港に紐づくタクシー会社かチェックする


        taxiService.updateTaxi(taxiInfo)
        return "redirect:/taxi/list"
    }


}