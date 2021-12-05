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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

/**
 * 書籍コントローラのテストクラス
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class BookControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private val mapper: ObjectMapper = ObjectMapper()

    /**
     * 書籍リストを取得する
     */
    @Test
    fun testGetBooks(){
        val result: MvcResult = mockMvc
            .perform(get("/books"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books[0].title shouldBe "refactoring"
        books[1].title shouldBe "clean architecture"
        books[2].title shouldBe "clean code"
        books[3].title shouldBe "clean agile"

        books[0].author shouldBe "Martin Fowler"
        books[1].author shouldBe "Robert Martin"
        books[2].author shouldBe "Robert Martin"
        books[3].author shouldBe "Robert Martin"
    }

    /**
     * 指定した書籍を取得する
     */
    @Test
    fun testGetOneBook(){
        val result: MvcResult = mockMvc
            .perform(get("/books/6"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val book: Book  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<Book>())
        book.title shouldBe "refactoring"
        book.author shouldBe "Martin Fowler"
    }

    /**
     * 指定した書籍を取得する（存在しないID）
     */
    @Test
    fun testGetOneBook2(){
        mockMvc
            .perform(get("/books/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    /**
     * 書籍を検索する
     */
    @Test
    fun testSearchBookTitle(){
        val result: MvcResult = mockMvc
            .perform(get("/books?title=clean"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 3
        books[0].title shouldBe "clean architecture"
        books[1].title shouldBe "clean code"
        books[2].title shouldBe "clean agile"
        books[0].author shouldBe "Robert Martin"
        books[1].author shouldBe "Robert Martin"
        books[2].author shouldBe "Robert Martin"
    }

    /**
     * 書籍を検索する（存在しないタイトル）
     */
    @Test
    fun testSearchBookTitle2(){
        val result: MvcResult = mockMvc
            .perform(get("/books?title=hoge"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 0
    }

    /**
     * 著者を検索する
     */
    @Test
    fun testSearchBookAuthor(){
        val result: MvcResult = mockMvc
            .perform(get("/books?author=Robert"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books[0].title shouldBe "clean architecture"
        books[1].title shouldBe "clean code"
        books[2].title shouldBe "clean agile"
        books[0].author shouldBe "Robert Martin"
        books[1].author shouldBe "Robert Martin"
        books[2].author shouldBe "Robert Martin"
    }

    /**
     * 著者を検索する（存在しない著者）
     */
    @Test
    fun testSearchBookAuthor2(){
        val result: MvcResult = mockMvc
            .perform(get("/books?author=hoge"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val books: List<Book>  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 0
    }

    /**
     * 書籍を登録する
     */
    @Test
    fun testCreateBook(){
        val book = Book(10, "java code","john smith")
        val mapper = ObjectMapper()
        val json: String = mapper.writeValueAsString(book)
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    /**
     * 書籍を更新する
     */
    @Test
    fun testUpdateBook(){
        val book = Book(6, "refactoring2","Martin Fowler")
        val mapper = ObjectMapper()
        val json: String = mapper.writeValueAsString(book)
        mockMvc
            .perform(
                post("/books/6")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

}