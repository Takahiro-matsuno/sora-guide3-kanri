package jp.co.jalinfotec.soraguide.airportmaintenance.util

/**
 * 共通定数
 */
object Constant {

    const val ACCOUNT_ROCK_COUNT = 5
    // 環境変数から取得する
    val FROM_ADDRESS = System.getenv("FROM_ADDRESS")

    enum class MAIL_TYPE { RESERVE, CANCEL, RESET, NONE }

    val mailSubject = mutableMapOf(
            MAIL_TYPE.RESERVE to "ご予約が確定しました",
            MAIL_TYPE.CANCEL to "取消完了のお知らせ",
            MAIL_TYPE.RESET to "アカウントリセットのお知らせ"
    )

    val mailContent = mutableMapOf(
            MAIL_TYPE.RESERVE to
                    "%name% 様\n" +
                    "\n" +
                    "タクシーをご予約いただきありがとうございます。\n" +
                    "\n" +
                    "■ご予約内容\n" +
                    "\n" +
                    "＜予約番号＞\n" +
                    "%rsvId%\n" +
                    "\n" +
                    "＜予約情報＞\n" +
                    "乗車日付：%rideOnDate%\n" +
                    "乗車時刻：%rideOnTime%\n" +
                    "\n" +
                    "■ご案内\n" +
                    "ご予約の変更をご希望のお客様は下記URLから変更いただくか、タクシー会社へ直接ご連絡ください。\n" +
                    "%url%\n" +
                    "\n" +
                    "■お問い合わせ\n" +
                    "タクシー会社：%companyName%\n" +
                    "お電話：%companyContact%\n" +
                    "\n" +
                    "\n" +
                    "当メールは送信専用です。\n" +
                    "\n",

            MAIL_TYPE.CANCEL to
                    "%name% 様\n" +
                    "\n" +
                    "タクシーをご予約いただきありがとうございます。\n" +
                    "以下のご予約のお取消しを承りました。\n" +
                    "またのご利用をお待ちしております。\n" +
                    "\n" +
                    "■お取消済み内容\n" +
                    "\n" +
                    "＜予約番号＞\n" +
                    "%rsvId%\n" +
                    "\n" +
                    "＜予約情報＞\n" +
                    "乗車日付：%rideOnDate%\n" +
                    "乗車時刻：%rideOnTime%\n" +
                    "\n" +
                    "\n" +
                    "■お問い合わせ\n" +
                    "タクシー会社：%companyName%\n" +
                    "お電話：%companyContact%\n" +
                    "\n" +
                    "\n" +
                    "当メールは送信専用です。\n" +
                    "\n",

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