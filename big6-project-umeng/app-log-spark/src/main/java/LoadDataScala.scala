import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 加载数据到hive，实现转储
  */
object LoadDataScala {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("loadDataScala")
        val sess = SparkSession.builder().config(conf).getOrCreate()
        import sess.implicits._

        sess.sql("use umengdb").show()

        if (args != null && args.length == 4) {
            val logtype = args(0)
            val ym = args(1)
            val day = args(2)
            val hm = args(3)

            def doload(logType: String, ym: String, day: String, hm: String) = {
                var sqltmp = "load data inpath '/user/centos/applogs/#logtype#/#ym#/#day#/#hm#' into table ext_#logtype#_logs partition(ym='#ym#',day='#day#',hm='#hm#')"
                sqltmp = sqltmp.replace("#logtype#", logType)
                sqltmp = sqltmp.replace("#ym#", ym)
                sqltmp = sqltmp.replace("#day#", day)
                sqltmp = sqltmp.replace("#hm#", hm)
                sess.sql(sqltmp).show
            }

            def doDump(sql0: String, ym: String, day: String, hm: String) = {
                var sql = sql0
                sql = sql.replace("#ym#", ym)
                sql = sql.replace("#day#", day)
                sql = sql.replace("#hm#", hm)
                sess.sql(sql).show
            }

            //加载数据文件到hive表
            doload(logtype, ym, day, hm)

            //转储数据表dump
            var sql = ""
            logtype match {
                case "startup" =>
                    sql = "insert into ext_startup_logs_par partition(ym='#ym#',day='#day#',hm='#hm#') select createdAtMs ,appId ,tenantId ,deviceId ,appVersion ,appChannel ,appPlatform ,osType ,deviceStyle ,country ,province ,ipAddress ,network ,carrier ,brand ,screenSize from ext_startup_logs where ym = '#ym#' and day = '#day#' and hm = '#hm#'"
                case "error" =>
                    sql = "insert into ext_error_logs_par partition(ym='#ym#',day='#day#',hm='#hm#') select createdAtMs ,appId ,tenantId ,deviceId ,appVersion ,appChannel ,appPlatform ,osType ,deviceStyle ,errorBrief ,errorDetail from ext_error_logs where ym = '#ym#' and day = '#day#' and hm = '#hm#'"
                case "event" =>
                    sql = "insert into ext_event_logs_par partition(ym='#ym#',day='#day#',hm='#hm#') select createdAtMs big,appId ,tenantId ,deviceId ,appVersion ,appChannel ,appPlatform ,osType ,deviceStyle ,eventId ,eventDurationSecs,paramKeyValueMap from ext_event_logs where ym = '#ym#' and day = '#day#' and hm = '#hm#'"
                case "usage" =>
                    sql = "insert into ext_usage_logs_par partition(ym='#ym#',day='#day#',hm='#hm#') select createdAtMs ,appId ,tenantId ,deviceId ,appVersion ,appChannel ,appPlatform ,osType ,deviceStyle ,singleUseDurationSecs ,singleUploadTraffic ,singleDownloadTraffic from ext_usage_logs where ym = '#ym#' and day = '#day#' and hm = '#hm#'"
                case "page" =>
                    sql = "insert into ext_page_logs_par partition(ym='#ym#',day='#day#',hm='#hm#') select createdAtMs ,appId ,tenantId ,deviceId ,appVersion ,appChannel ,appPlatform ,osType ,deviceStyle ,pageViewCntInSession ,pageId ,visitIndex ,nextPage ,stayDurationSecs from ext_page_logs where ym = '#ym#' and day = '#day#' and hm = '#hm#'"
            }
            doDump(sql, ym, day, hm)
        }
        else {
            println("参数不足！！")
        }
    }
}
