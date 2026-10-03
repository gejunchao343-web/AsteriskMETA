object InnerProfileFactory {
    fun getBuildInProfile(): MihomoProfile {
        val yamlText = """
mixed-port: 7890
allow-lan: true
log-level: info
external-controller: 127.0.0.1:9090
dns:
  enable: true
  listen: 0.0.0.0:53
  nameserver:
    - 223.5.5.5
    - 114.114.114.114

proxies: []

rules:
  - DOMAIN-SUFFIX,tw.pool.ntp.org,DIRECT
  - DOMAIN-SUFFIX,pool.ntp.org,DIRECT
  - DOMAIN-SUFFIX,cn.pool.ntp.org,DIRECT
  - DOMAIN-SUFFIX,time.asia.apple.com,DIRECT
  - DOMAIN-SUFFIX,ntp1.aliyun.com,DIRECT
  - DOMAIN-SUFFIX,hk.pool.ntp.org,DIRECT
  - DOMAIN-SUFFIX,asia.pool.ntp.org,DIRECT
  - DOMAIN-SUFFIX,jp.pool.ntp.org,DIRECT
  - DOMAIN-SUFFIX,sg.pool.ntp.org,DIRECT
  - FINAL,DIRECT
""".trimIndent()

        return MihomoProfile(
            name = "内置NTP规则配置",
            content = yamlText
        )
    }
}