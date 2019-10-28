package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TopicsForm
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/topics")
class TopicsRegistryController {

    /**
     * トピックス登録画面
     */
    // 画面表示
    @GetMapping("/reg")
    fun getRegistry(
            @ModelAttribute form: TopicsForm,
            model: Model
    ): String {
        return "topics/topics-reg"
    }

    // 登録
    @PostMapping("/reg")
    fun postRegistry(
            @ModelAttribute form: TopicsForm,
            bindingResult: BindingResult,
            model: Model
    ) : String {
        return if (bindingResult.hasErrors()) {
            getRegistry(form, model)
        } else {
            "redirect:/topics-list"
        }
    }
}