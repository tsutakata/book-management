## 書籍管理サンプルアプリケーション
〜Kotlin + Spring Boot + JPA(Hibernate) によるサンプルアプリケーション〜

#### 概要
書籍を管理するサンプルアプリケーション
- 書籍には著者の属性があり、書籍と著者の情報をRDBに登録・変更・検索ができる
- 著者に紐づく本を取得できる

#### 詳細
提供するAPIは以下のとおり。
- 参照API
  - すべての書籍情報を参照する
  - 指定された書籍IDに該当する書籍情報を参照する
  - 書名を指定して、書籍情報を検索する（部分一致）
  - 著者名を指定して、書籍情報を検索する（部分一致）
- 更新API
  - 書籍情報を新規登録する（書名、著者名を必ず指定）
  - 書籍情報を更新する（書名、著者名のいずれかを指定）

#### URL
提供するAPIのURLは以下のとおり。

|HTTPメソッド  |URL  |内容  |
|---|---|---|
|GET  |/Books  |すべての書籍情報を参照する  |
|POST  |/Books  |書籍情報を新規登録する  |
|GET  |/Books/{id}  |指定した書籍情報を参照する  |
|POST  |/Books/{id}  |指定した書籍情報を更新する  |
|GET  |/Books?title={title}  |書名で検索する  |
|GET  |/Books?author={author}  |著者名で検索する  |

#### データベース
参照するデータベースは以下のとおり。  
Bookテーブル

|カラム名  |型  |必須  |PK  |
|---|---|---|---|
|ID  |数値  |◯  |◯|
|書籍名  |文字列  |◯  ||
|著者名  |文字列  |◯  ||
