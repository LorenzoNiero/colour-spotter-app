package com.challenge.colour_spotter.domain.domain.repository

import com.challenge.colour_spotter.data.datasource.ColorLocalDataSource
import com.challenge.colour_spotter.domain.mock.DataMock
import com.challenge.colour_spotter.domain.repository.ColorRepository
import com.challenge.colour_spotter.domain.repository.ColorRepositoryImpl
import com.challenge.colour_spotter.network.data.NetworkDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class RepositoryTest {
    @MockK
    private lateinit var networkDataSource: NetworkDataSource

    @MockK
    private lateinit var colorLocalDataSource: ColorLocalDataSource

    private lateinit var repository: ColorRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
        repository = ColorRepositoryImpl(
            networkDataSource,
            colorLocalDataSource
        )
    }

    @Test
    fun `fetchColorInfo should call networkDataSource and return with success`() = runTest {
        // Given
        val colorNetwork = DataMock.colorNetwork
        coEvery { networkDataSource.fetchColorInfo(any()) } returns colorNetwork

        // When
        repository.fetchColorInfo("#fff")

        // Then
        coVerify { networkDataSource.fetchColorInfo(any()) }
    }

    @Test
    fun `fetchColorInfo throws IllegalArgumentException for invalid color code`() = runTest {
        assertFailsWith<IllegalArgumentException> {
            repository.fetchColorInfo("error")
        }
    }

    @Test
    fun `fetchColorInfo with hex code valid should call networkDataSource and throw exception`() = runTest {
        // Given
        val colorNetwork = DataMock.colorNetwork
        coEvery { networkDataSource.fetchColorInfo(any()) } throws Exception()

        // When
        assertFailsWith<Exception> {
            repository.fetchColorInfo("#fff")
        }

        // Then
        coVerify { networkDataSource.fetchColorInfo(any()) }
    }


}