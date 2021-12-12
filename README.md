## 書籍管理サンプルアプリケーション
〜Kotlin + Spring Boot + Spring Data JPA(Hibernate) によるREST APIサンプルアプリケーション〜

### 概要
書籍を管理するサンプルアプリケーション
- 書籍には著者の属性があり、書籍と著者の情報をRDBに登録・変更・検索ができる
- 著者に紐づく本を取得できる

### 詳細
提供するAPIは以下のとおり。
- 参照API
  - すべての書籍情報を参照する
  - 指定された書籍IDに該当する書籍情報を参照する
  - 書名を指定して、書籍情報を検索する（部分一致）
  - 著者名を指定して、書籍情報を検索する（部分一致）
  - 著者名を指定して、著者に紐づく書名リストを取得する（完全一致）
- 更新API
  - 書名、著者名を指定して、書籍情報を新規登録する
  - 書名、著者名を指定して、書籍情報を更新する

### API
提供するAPIは以下のとおり。

|HTTPメソッド  |URL  |内容  |
|---|---|---|
|GET  |/Books  |すべての書籍情報を参照する  |
|POST  |/Books  |書籍情報を新規登録する  |
|GET  |/Books/{id}  |指定した書籍IDに該当する書籍情報を参照する  |
|POST  |/Books/{id}  |指定した書籍IDに該当する書籍情報を更新する  |
|GET  |/Books?title={title}  |書名を条件に指定して書籍情報を検索する  |
|GET  |/Books?author={author}  |著者名を条件に指定して書籍情報を検索する  |
|GET  |/Books?author={author}&field={field}  |著者名を条件に指定して書名リストを取得する  |

リクエスト例は以下のとおり。
```
// 新規登録
curl -X POST -H "Content-Type: application/json" -d '{"title":"hoge", "author":"fuga"}' http://localhost:8080/books

// 参照
curl http://localhost:8080/books
curl http://localhost:8080/books/1

// 検索
curl http://localhost:8080/books?title=hoge
curl http://localhost:8080/books?author=fuga
curl http://localhost:8080/books?author=fuga\&field=title

// 更新
curl -X POST -H "Content-Type: application/json" -d '{"title":"hoge2", "author":"fuga2"}' http://localhost:8080/books/1
```

### データベース
参照するデータベースは以下のとおり。  

Bookテーブル

|カラム名  |型  |必須  |PK  |
|---|---|---|---|
|ID  |数値  |◯  |◯|
|書籍名  |文字列  |◯  ||
|著者名  |文字列  |◯  ||

- H2 Databaseを利用しているため、設定は特に不要。
- ローカル起動時に上記テーブルを作成するように設定済み。

### 備考
以下について、今回は省略している。
- 1冊の書籍を複数の著者が執筆する書籍情報の取り扱い
- サービスクラスの実装（ロジックがほとんどないため）
- コマンドとクエリの分離（複雑なクエリがないため）
- Bookテーブルの登録日時、更新日時の設定
- エンティティ、リポジトリクラスのテスト
- swaggerなどによるAPI仕様書生成
- flywayなどによるDBマイグレーションの設定

### 参考URL
##### 公式
[Kotlin](https://kotlinlang.org/)  
[Spring Boot](https://spring.io/projects/spring-boot)  
[Spring Data JPA](https://spring.io/projects/spring-data-jpa)  
[Spring Framework > 言語サポート > Kotlin](https://spring.pleiades.io/spring-framework/docs/current/reference/html/languages.html#languages)  
[Kotlin で Spring Boot Web アプリケーションの作成](https://spring.pleiades.io/guides/tutorials/spring-boot-kotlin/)  

##### blog
[雑にKotlinでSpring触ってみた件](https://blog.morifuji-is.ninja/post/2020-04-29/)  
[SpringBootでのテストについて触った結果をまとめてみた](https://blog.morifuji-is.ninja/post/2020-07-23/)  
[SpringBootのDataJPAに戦術的DDDの要素を導入するにはどうすれば良いか調べてみた](https://blog.morifuji-is.ninja/post/2021-04-23/)  
[Spring + JPAによるアプリケーション構築事例](https://medium.com/finatext/example-of-application-building-with-spring-jpa-6e52d456f5cd)  
