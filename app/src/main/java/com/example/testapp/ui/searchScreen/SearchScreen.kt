package com.example.testapp.ui.searchScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testapp.R
import com.example.testapp.domain.RetrofitInstance
import com.example.testapp.models.SearchResult

const val minQueryLength = 3 // минимальная длина строки для выполнения поиска

@Preview(showBackground = true)
@Composable
fun SearchScreen(navController: NavController = rememberNavController()) {
    // remember сохраняет значение переменной в памяти, чтобы оно не сбрасывалось, пока компонент жив
    // mutableStateOf используется для создания состояния, которое может изменяться и будет автоматически отслеживаться
    var query by remember { mutableStateOf("") }                        // текст запроса
    var uniqueKey by remember { mutableStateOf(0) }                     // костыль, чтобы триггерилась кнопка Обновить
    val searchResults =
        remember { mutableStateListOf<SearchResult>() }                       // полученный результат коллекция SearchResult
    var isLoading by remember { mutableStateOf(false) }                 // флаг загрузки
    var errorMessage by remember { mutableStateOf<String?>(null) }      // текст ошибки

    // LaunchedEffect позволяет выполнить код, который должен быть вызван один раз
    // при изменении ключевого параметра или состояния.
    LaunchedEffect(uniqueKey) {
        // TODO: дебаунсер добавить из за слишком частых запросов выдает 403
        if (query.length >= minQueryLength) {
            isLoading = true        // ставим флаг, что загрузка в процессе
            errorMessage = null     // обнуляем ошибку
            searchResults.clear()   // очищаем существующий результат поиска

            try {
                val userResponse =
                    RetrofitInstance.api.searchUsers(query)          // выполняем поиск по юзерам
                val repoResponse =
                    RetrofitInstance.api.searchRepositories(query)   // выполняем поиск по репозиториям

                // добавляем полученные данные в массив результатов
                searchResults.addAll(userResponse.items)
                searchResults.addAll(repoResponse.items)

                isLoading = false // ставим флаг, что загрузка завершена
            } catch (e: Exception) {
                // в случае ошибки - устанавливаем сообщение в errorMessage
                errorMessage = e.localizedMessage ?: "Неизвестная ошибка"
                isLoading = false // снимаем флаг загрузки
            }
        } else {
            // если символов <3 - очищаем поле вывода
            searchResults.clear()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { newValue -> query = newValue; uniqueKey++},
            label = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.Black,
            ),
            leadingIcon = { // впереди иконка Лупа
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "Иконка Лупа",
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_clear_24),
                    contentDescription = "Очистить поле ввода",
                    modifier = Modifier.clickable {
                        query = ""
                    }
                )
            },
        )

        if (query.length < 3) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Поиск",
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)
                    )

                    Text(
                        text = "Начните вводить поисковой запрос (от 3-х символов), чтобы увидеть его результаты \uD83D\uDE1E",
                        color = Gray,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )

                    Image(
                        colorFilter = ColorFilter.tint(Gray),
                        painter = painterResource(id = R.drawable.baseline_sticky_note_2_24),
                        contentDescription = "Пустое поле поиска",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Показать состояние загрузки, если она выполняется
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Если errorMessage != null (произошла ошибка) показываем сообщение с кнопкой Обновить
        errorMessage?.let {
            Image(
                painter = painterResource(id = R.drawable.baseline_question_mark_24),
                contentDescription = "Ничего не найдено",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentScale = ContentScale.None
            )
            Text(
                text = "Тут ничего нет",
                color = Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )
            Text(
                text = "Возможно чуть позже здесь что-то появится",
                color = Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text( // для отладки
                text = it,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )
            Button(
                onClick = {
                    // Повторить попытку загрузки
                    // Триггерим повторную загрузку с текущим запросом
                    // TODO: доделать!
                    val prevQuery = query
                    query = ""
                    query = prevQuery
                    uniqueKey++ // костыль, чтобы триггерить обновление экрана
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Text("Повторить попытку")
            }
        }

        // Показать результаты поиска
        LazyColumn {// аналог RecyclerView
            val sortedResults = searchResults.sortedBy {
                // сортировка
                // Данные выводятся в виде одного списка отсортированные по алфавиту
                // на основе user.login и repository.name
                when (it) {
                    is SearchResult.User -> it.login.lowercase()
                    is SearchResult.Repository -> it.name.lowercase()
                }
            }

            items(sortedResults) { result ->
                SearchItem(result, navController)
            }
        }
    }
}
