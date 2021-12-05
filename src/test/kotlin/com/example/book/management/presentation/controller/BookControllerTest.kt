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

    @Test
    fun `すべての書籍情報リストが取得できる`(){
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

    @Test
    fun `存在するIDを指定すると、特定の書籍情報が取得できる`(){
        val result: MvcResult = mockMvc
            .perform(get("/books/6"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val book: Book  = mapper.readValue(result.response.contentAsString, jacksonTypeRef<Book>())
        book.title shouldBe "refactoring"
        book.author shouldBe "Martin Fowler"
    }

    @Test
    fun `存在しないIDを指定すると、書籍情報が取得できない`(){
        mockMvc
            .perform(get("/books/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `存在する書名で検索すると、該当する書名の書籍情報リストが返る`(){
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
        books[0].title shouldBe "clean architecture"
        books[1].title shouldBe "clean code"
        books[2].title shouldBe "clean agile"
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
    fun `書籍情報を新規登録できる`(){
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

    @Test
    fun `書籍情報を更新できる`(){
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