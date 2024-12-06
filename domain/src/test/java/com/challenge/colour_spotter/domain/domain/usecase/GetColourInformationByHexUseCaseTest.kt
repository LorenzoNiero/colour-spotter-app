package com.challenge.colour_spotter.domain.domain.usecase

import com.challenge.colour_spotter.domain.mock.DomainMock
import com.challenge.colour_spotter.domain.repository.ColorRepository
import com.challenge.colour_spotter.domain.usecase.GetColourInformationByHexUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetColourInformationByHexUseCaseTest {

    @MockK
    private lateinit var repository: ColorRepository

    private lateinit var useCase: GetColourInformationByHexUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = GetColourInformationByHexUseCase(repository)
    }

    @Test
    fun `use case should call repository without error`() = runTest {
        // Given
        coEvery { repository.fetchColorInfo(any()) } returns DomainMock.colorWhite

        // When
        val response = useCase("#fffff")

        // Then
        coVerify { repository.fetchColorInfo(any()) }
        assertEquals(response.getOrThrow(), DomainMock.colorWhite)
    }

    @Test
    fun `use case should call repository passed a bad hex code and return with error`() = runTest {
        // Given
        coEvery { repository.fetchColorInfo(any()) } throws IllegalArgumentException()

        // When
        val response = useCase("errorColor")

        // Then
        coVerify { repository.fetchColorInfo(any()) }
        assert(response.exceptionOrNull() is IllegalArgumentException)
        assert(response.isFailure)
    }
}

