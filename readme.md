##書籍管理サンプルアプリ
〜Kotlin * Spring Boot によるサンプル〜

####概要
書籍を管理するサンプルアプリ

####詳細
- 参照API
  - 指定された書籍を参照する
  - すべての書籍リストを参照する
  - 指定された著者を参照する
  - すべての著者リストを参照する
  - 指定された著者にひもづく書籍リストを参照する
- 更新API
  - 書籍を登録する
  - 書籍を更新する
  - 著者を登録する
  - 著者を更新する

####データベース
Bookテーブル

|カラム名  |型  |必須  |
|---|---|---|
|ID  |数値  |◯  |
|書籍名  |文字列  |◯  |
|著者名  |文字列  |◯  |

####動作確認
- git clone
- gradle build
- 
####技術構成
- Kotlin
- Spring Boot
- Spring Data JPA
- Kotest
