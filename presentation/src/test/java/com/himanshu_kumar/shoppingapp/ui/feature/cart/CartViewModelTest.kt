package com.himanshu_kumar.shoppingapp.ui.feature.cart

import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.domain.usecase.DeleteProductUseCase
import com.himanshu_kumar.domain.usecase.GetCartUseCase
import com.himanshu_kumar.domain.usecase.UpdateQuantityUseCase
import com.himanshu_kumar.shoppingapp.AppSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.mockito.kotlin.whenever


class CartViewModelTest:KoinTest {

    val cartUseCase: GetCartUseCase by inject()
    val updateQuantityUseCase: UpdateQuantityUseCase by inject()
    val deleteProductUseCase: DeleteProductUseCase by inject()
    val appSession: AppSession by inject()

    val classToTest :CartViewModel by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { cartUseCase }
                single { updateQuantityUseCase }
                single { deleteProductUseCase }
                single { appSession }
                single{ CartViewModel(get(),get(),get(),get()) }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(appSession.getUser()).thenReturn(
            1
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    fun incrementQuantity() {
    }

    @Test
    fun decrementQuantity() {
    }
}