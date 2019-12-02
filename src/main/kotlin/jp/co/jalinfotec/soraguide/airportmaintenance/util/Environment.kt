package jp.co.jalinfotec.soraguide.airportmaintenance.util

/**
 * 環境変数
 */
object Environment {
    val FROM_ADDRESS = System.getenv("FROM_ADDRESS")
    val AZURE_STORAGE_KEY = System.getenv("AZURE_STORAGE_KEY")
}