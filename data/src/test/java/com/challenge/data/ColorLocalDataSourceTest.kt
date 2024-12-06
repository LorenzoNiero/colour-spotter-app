package com.challenge.data

import app.cash.turbine.test
import com.challenge.colour_spotter.data.datasource.ColorLocalDataSourceImpl
import com.challenge.colour_spotter.database.dao.ColorDao
import com.challenge.colour_spotter.database.model.ColorEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ColorLocalDataSourceTest {
    private val colorDao: ColorDao = mockk()
    private val colorLocalDataSourceImpl = ColorLocalDataSourceImpl(colorDao)

    @Test
    fun `onColorsUpdate should emit colors from dao and return list`() = runBlocking {
        // Given
        val colorUpdateFlow: MutableSharedFlow<List<ColorEntity>> = MutableSharedFlow()
        coEvery { colorDao.observeAllColor() } returns colorUpdateFlow

        // When
        val colorsFlow = colorLocalDataSourceImpl.onColorsUpdate(true)

        // Then
        colorsFlow.test {
            colorUpdateFlow.emit(EntityMock.colorList)
            val result = awaitItem()

            // Then
            Assert.assertEquals(EntityMock.colorList.count(), result.count())
            Assert.assertEquals(EntityMock.colorList.first(), result.first())
        }
    }

    @Test
    fun `insertColor should call colorDao insert`() = runBlocking {
        // Given
        val color = ColorEntity("id1", "name1", "#ffffff")
        every { colorDao.insert(entity = any()) } returns Unit

        // When
        colorLocalDataSourceImpl.insertColor(color)

        // Then
        coVerify { colorDao.insert(color) }
    }

    @Test
    fun `deleteColor should call colorDao deleteById`() = runBlocking {
        // Given
        val colorId = "id1"
        coEvery { colorDao.deleteById(id = any()) } returns Unit

        // When
        colorLocalDataSourceImpl.deleteColor(colorId)

        // Then
        coVerify { colorDao.deleteById(colorId) }
    }

}

