# restful_jooby_jdbi
一個小的微框架jooby的測試，restful 資料交換

jooby是比較新的framework，而且是可獨立(內建web server)執行。
可編譯成war的方式執行。但是不支援tomcat太舊的版本(6 or 7)，
所以最後沒用。

jdbi api設計的挺親和的，相對於原生jdbc好用很多。

oracle 的 jdbc driver沒有放在 maven的公共 儲存庫裏，所以可以下載下來，
註冊在本機的 儲存庫使用。

設定連接參數，按框架 規範在conf目錄下。
