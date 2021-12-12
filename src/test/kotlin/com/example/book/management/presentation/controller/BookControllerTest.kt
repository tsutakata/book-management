package com.example.book.management.presentation.controller

import com.example.book.management.domain.model.Book
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

/**
 * 書籍コントローラのテストクラス
 */
@Suppress("NonAsciiCharacters")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class BookControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private val mapper: ObjectMapper = ObjectMapper()

    fun postNewBook(book: Book, expectStatus: ResultMatcher){
        val mapper = ObjectMapper()
        val json: String = mapper.writeValueAsString(book)
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(expectStatus)
            .andReturn()
    }

    fun postUpdateBook(book: Book, expectStatus: ResultMatcher): MvcResult{
        val mapper = ObjectMapper()
        val json: String = mapper.writeValueAsString(book)
        return mockMvc
            .perform(
                post("/books/${book.id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(expectStatus)
            .andReturn()
    }

    @Test
    fun `パラメータなしでリクエストすると、すべての書籍情報リストが取得できる`(){
        val result: MvcResult = mockMvc
            .perform(get("/books"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())

        // 検証
        // 書籍の冊数
        books.size shouldBe 4

        // ID
        books[0].id shouldBe 1
        books[1].id shouldBe 2
        books[2].id shouldBe 3
        books[3].id shouldBe 4

        // 書名
        books[0].title shouldBe "Refactoring"
        books[1].title shouldBe "Clean Architecture"
        books[2].title shouldBe "Clean Code"
        books[3].title shouldBe "Clean Agile"

        // 著者名
        books[0].author shouldBe "Martin Fowler"
        books[1].author shouldBe "Robert Martin"
        books[2].author shouldBe "Robert Martin"
        books[3].author shouldBe "Robert Martin"
    }

    @Test
    fun `パラメータに存在する書籍IDを指定してリクエストすると、特定の書籍情報が取得できる`(){
        val result: MvcResult = mockMvc
            .perform(get("/books/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val book: Book  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<Book>())
        book.id shouldBe 1
        book.title shouldBe "Refactoring"
        book.author shouldBe "Martin Fowler"
    }

    @Test
    fun `パラメータに存在しない書籍IDを指定してリクエストすると、書籍情報が取得できない`(){
        mockMvc
            .perform(get("/books/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `存在する書名で検索すると、該当する書名の書籍情報リストが取得できる`(){
        val result: MvcResult = mockMvc
            .perform(get("/books?title=Clean"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 3
        books[0].title shouldBe "Clean Architecture"
        books[1].title shouldBe "Clean Code"
        books[2].title shouldBe "Clean Agile"
        books[0].author shouldBe "Robert Martin"
        books[1].author shouldBe "Robert Martin"
        books[2].author shouldBe "Robert Martin"
    }

    @Test
    fun `存在しない書名で検索すると、空のJSONが返る`(){
        val result: MvcResult = mockMvc
            .perform(get("/books?title=hoge"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 0
    }

    @Test
    fun `存在する著者名で検索すると、その著者にひもづく書籍リストが返る`(){
        val result: MvcResult = mockMvc
            .perform(get("/books?author=Robert"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books[0].title shouldBe "Clean Architecture"
        books[1].title shouldBe "Clean Code"
        books[2].title shouldBe "Clean Agile"
        books[0].author shouldBe "Robert Martin"
        books[1].author shouldBe "Robert Martin"
        books[2].author shouldBe "Robert Martin"
    }

    @Test
    fun `存在しない著者名で検索すると、空のJSONが返る`(){
        val result: MvcResult = mockMvc
            .perform(get("/books?author=hoge"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 0
    }

    @Test
    fun `書名、著者名を入力すると、書籍情報を新規登録できる`(){
        val book = Book(null, "java code","john smith")
        postNewBook(book, MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `著者名のみ入力すると、書籍情報を新規登録できない`(){
        val book = Book(null, "","john smith")
        postNewBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `書名のみ入力すると、書籍情報を新規登録できない`(){
        val book = Book(null, "Java Tutorial","")
        postNewBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `書名、著者名ともにブランクでリクエストすると、書籍情報を新規登録できない`(){
        val book = Book(null, "","")
        postNewBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `書名、著者名を入力すると、書籍情報を更新できる`(){
        val book = Book(1, "Refactoring2","Martin Fowler")
        val result: MvcResult = postUpdateBook(book, MockMvcResultMatchers.status().isOk)
        val updateBook: Book  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<Book>())
        updateBook.title shouldBe "Refactoring2"
        updateBook.author shouldBe "Martin Fowler"
    }

    @Test
    fun `書名のみ入力すると、書籍情報を更新できない`(){
        val book = Book(1, "Refactoring2","")
        postUpdateBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `著者名のみ入力すると、書籍情報を更新できない`(){
        val book = Book(1, "","Martin Fowler")
        postUpdateBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

}