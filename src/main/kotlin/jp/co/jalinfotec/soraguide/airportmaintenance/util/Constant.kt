package jp.co.jalinfotec.soraguide.airportmaintenance.util

/**
 * 共通定数
 */
object Constant {

    const val ACCOUNT_ROCK_COUNT = 5
    enum class MAIL_TYPE { RESET }

    val mailSubject = mutableMapOf(
            MAIL_TYPE.RESET to "アカウントリセットのお知らせ"
    )

    val mailContent = mutableMapOf(
            MAIL_TYPE.RESET to
                    "%companyName% 管理者様\n" +
                    "\n" +
                    "以下のアカウントのパスワードをリセットしました。\n"+
                    "\n"+
                    "ユーザー名：%userName%\n"+
                    "新パスワード：%password%\n"+
                    "\n" +
                    "当メールは送信専用です。\n" +
                    "\n"
    )
}