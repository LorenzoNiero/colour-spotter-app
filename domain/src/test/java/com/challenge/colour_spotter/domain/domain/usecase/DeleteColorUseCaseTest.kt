package com.challenge.colour_spotter.domain.domain.usecase

import com.challenge.colour_spotter.domain.mock.DomainMock
import com.challenge.colour_spotter.domain.repository.ColorRepository
import com.challenge.colour_spotter.domain.usecase.DeleteColorUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class DeleteColorUseCaseTest {

    @MockK
    private lateinit var repository: ColorRepository

    private lateinit var useCase: DeleteColorUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = DeleteColorUseCase(repository)
    }

    @Test
    fun `use case should call repository without error`() = runTest {
        // Given
        coEvery { repository.deleteColor(any()) } returns Unit

        // When
        val response = useCase(DomainMock.colorWhite)

        // Then
        coVerify { repository.deleteColor(any()) }
    }

    @Test
    fun `use case should call repository with exception`() = runTest {
        // Given
        coEvery { repository.deleteColor(any()) } throws IllegalArgumentException()

        // When, then
        assertFailsWith<IllegalArgumentException> {
            useCase(DomainMock.colorWhite)
        }
    }
}