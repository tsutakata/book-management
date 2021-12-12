package com.example.book.management.presentation.controller

import com.example.book.management.domain.model.Book
import com.example.book.management.domain.model.BookRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
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

    /**
     * すべての書籍を取得する
     */
    fun getAllBooks(expectStatus: ResultMatcher): MvcResult =
        mockMvc
            .perform(get("/books"))
            .andExpect(expectStatus)
            .andReturn()

    /**
     * 指定したIDに該当する書籍を取得する
     */
    fun getBookById(id: Long, expectStatus: ResultMatcher): MvcResult =
        mockMvc
            .perform(get("/books/${id}"))
            .andExpect(expectStatus)
            .andReturn()

    /**
     * 書名・著者名に部分一致する書籍を取得する
     */
    fun getBookByTitleOrAuthor(column: String, keyword: String, expectStatus: ResultMatcher): MvcResult =
        mockMvc
            .perform(get("/books?${column}=${keyword}"))
            .andExpect(expectStatus)
            .andReturn()

    /**
     * 著者名に完全に一致する書籍を取得する
     */
    fun getBookTitlesByAuthor(keyword: String, field: String, expectStatus: ResultMatcher): MvcResult =
        mockMvc
            .perform(get("/books?author=${keyword}&field=${field}"))
            .andExpect(expectStatus)
            .andReturn()

    /**
     * 書籍を新規に登録する
     */
    fun postNewBook(book: BookRequest, expectStatus: ResultMatcher): MvcResult =
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(book))
            )
            .andExpect(expectStatus)
            .andReturn()

    /**
     * 書籍を更新する
     */
    fun postUpdateBook(book: BookRequest, expectStatus: ResultMatcher): MvcResult =
        mockMvc
            .perform(
                post("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(book))
            )
            .andExpect(expectStatus)
            .andReturn()

    @Test
    fun `パラメータなしでGETリクエストすると、すべての書籍情報が取得できる`() {
        val result: MvcResult = getAllBooks(MockMvcResultMatchers.status().isOk)
        val books: List<Book> = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 4
        books[0] shouldBeEqualToComparingFields Book(1, "Refactoring", "Martin Fowler")
        books[1] shouldBeEqualToComparingFields Book(2, "Clean Architecture", "Robert Martin")
        books[2] shouldBeEqualToComparingFields Book(3, "Clean Code", "Robert Martin")
        books[3] shouldBeEqualToComparingFields Book(4, "Clean Agile", "Robert Martin")
    }

    @Test
    fun `存在するIDを指定してGETリクエストすると、該当する書籍情報が取得できる`() {
        val result: MvcResult = getBookById(1, MockMvcResultMatchers.status().isOk)
        val book: Book = mapper.readValue(result.response.contentAsString, jacksonTypeRef<Book>())
        book shouldBeEqualToComparingFields Book(1, "Refactoring", "Martin Fowler")
    }

    @Test
    fun `存在しないIDを指定してGETリクエストすると、書籍情報が取得できない`() {
        getBookById(999, MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `存在する書名で検索すると、該当する書名の書籍情報が取得できる`() {
        val result: MvcResult = getBookByTitleOrAuthor("title", "Clean", MockMvcResultMatchers.status().isOk)
        val books: List<Book> = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 3
        books[0] shouldBeEqualToComparingFields Book(2, "Clean Architecture", "Robert Martin")
        books[1] shouldBeEqualToComparingFields Book(3, "Clean Code", "Robert Martin")
        books[2] shouldBeEqualToComparingFields Book(4, "Clean Agile", "Robert Martin")
    }

    @Test
    fun `存在しない書名で検索すると、空のJSONが返る`() {
        val result: MvcResult =
            getBookByTitleOrAuthor("title", "hoge", MockMvcResultMatchers.status().isOk)
        val books: List<Book> = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 0
    }

    @Test
    fun `存在する著者名で部分一致で検索すると、その著者に該当する書籍情報が取得できる`() {
        val result: MvcResult =
            getBookByTitleOrAuthor("author", "Robert", MockMvcResultMatchers.status().isOk)
        val books: List<Book> = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books[0] shouldBeEqualToComparingFields Book(2, "Clean Architecture", "Robert Martin")
        books[1] shouldBeEqualToComparingFields Book(3, "Clean Code", "Robert Martin")
        books[2] shouldBeEqualToComparingFields Book(4, "Clean Agile", "Robert Martin")
    }

    @Test
    fun `存在しない著者名で部分一致で検索すると、空のJSONが返る`() {
        val result: MvcResult =
            getBookByTitleOrAuthor("author", "hoge", MockMvcResultMatchers.status().isOk)
        val books: List<Book> = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<Book>>())
        books.size shouldBe 0
    }

    @Test
    fun `存在する著者名で完全一致で検索すると、その著者名の書名リストが取得できる`() {
        val result: MvcResult =
            getBookTitlesByAuthor("Robert Martin", "title", MockMvcResultMatchers.status().isOk)
        val titles: List<String> = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<String>>())
        titles.size shouldBe 3
        titles[0] shouldBe "Clean Architecture"
        titles[1] shouldBe "Clean Code"
        titles[2] shouldBe "Clean Agile"
    }

    @Test
    fun `著者名が部分的に一致するリクエストをしても、その著者名の書名リストが取得できない`() {
        val result: MvcResult =
            getBookTitlesByAuthor("Robert", "title", MockMvcResultMatchers.status().isOk)
        val titles: List<String> = mapper.readValue(result.response.contentAsString, jacksonTypeRef<List<String>>())
        titles.size shouldBe 0
    }

    @Test
    fun `fieldの指定をtitle以外でリクエストすると、その著者名の書名リストが取得できない`() {
        getBookTitlesByAuthor("Robert Martin", "author", MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `書名、著者名を入力してPOSTリクエストすると、書籍情報を新規登録できる`() {
        val book = BookRequest("java code", "john smith")
        postNewBook(book, MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `著者名のみ入力してPOSTリクエストしても、書籍情報を新規登録できない`() {
        val book = BookRequest("", "john smith")
        postNewBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `書名のみ入力してPOSTリクエストしても、書籍情報を新規登録できない`() {
        val book = BookRequest("Java Tutorial", "")
        postNewBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `書名、著者名ともにブランクでリクエストすると、書籍情報を新規登録できない`() {
        val book = BookRequest("", "")
        postNewBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `書名、著者名を入力してPOSTリクエストすると、書籍情報を更新できる`() {
        val book = BookRequest("Refactoring2", "Martin Fowler")
        val result: MvcResult = postUpdateBook(book, MockMvcResultMatchers.status().isOk)
        val updateBook: Book = mapper.readValue(result.response.contentAsString, jacksonTypeRef<Book>())
        updateBook.title shouldBe "Refactoring2"
        updateBook.author shouldBe "Martin Fowler"
    }

    @Test
    fun `書名のみ入力してPOSTリクエストしても、書籍情報を更新できない`() {
        val book = BookRequest("Refactoring2", "")
        postUpdateBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `著者名のみ入力してPOSTリクエストしても、書籍情報を更新できない`() {
        val book = BookRequest("", "Martin Fowler")
        postUpdateBook(book, MockMvcResultMatchers.status().isBadRequest)
    }

}