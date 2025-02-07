package com.example.testapp.ui.searchScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.formatDate
import com.example.testapp.models.SearchResult

/*
    result: SearchResult = SearchResult.User(
    login = "lemmiwinks1551",
    avatarUrl = "https://avatars.githubusercontent.com/u/94862109?v=4",
    profileUrl = "https://github.com/lemmiwinks1551" )

    result: SearchResult = SearchResult.Repository(
    name = "MySchedule",
    stars = 2,
    watchers = 2,
    forks = 0,
    owner = SearchResult.User(
        login = "lemmiwinks1551",
        avatarUrl = "https://avatars.githubusercontent.com/u/94862109?v=4",
        profileUrl = "https://github.com/lemmiwinks1551"
    ),
    createdAt = "2022-06-14T19:07:28Z",
    updatedAt = "2025-01-24T08:06:44Z",
    description = "Самый лучший репозиторий",
    repoUrl = "https://github.com/lemmiwinks1551/MySchedule")
    */

@Preview(showBackground = true)
@Composable
fun SearchItem(
    result: SearchResult = SearchResult.Repository(
        name = "MySchedule",
        stars = 2,
        watchers = 2,
        forks = 0,
        owner = SearchResult.User(
            login = "lemmiwinks1551",
            avatarUrl = "https://avatars.githubusercontent.com/u/94862109?v=4",
            profileUrl = "https://github.com/lemmiwinks1551"
        ),
        createdAt = "2022-06-14T19:07:28Z",
        updatedAt = "2025-01-24T08:06:44Z",
        description = "Самый лучший репозиторий",
        repoUrl = "https://github.com/lemmiwinks1551/MySchedule"
    ),
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current

    when (result) {
        is SearchResult.User -> UserCard(result, context)
        is SearchResult.Repository -> RepositoryCard(result, navController, context)
    }
}

@Composable
fun UserCard(result: SearchResult.User, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp) // внутренний padding
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(11.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.profileUrl))
                    context.startActivity(intent)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = result.avatarUrl,
                contentDescription = "Аватар пользователя",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RectangleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = result.login, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(0.8f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Открыть профиль в браузере"
            )
        }
    }
}

@Composable
fun RepositoryCard(
    result: SearchResult.Repository,
    navController: NavController,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp) // внешний padding
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {
                // кликнули по карточке - переходим в репозиторий
                // в маршрут подставляет login владельца и name репозитория
                navController.navigate("repository/${result.owner.login}/${result.name}")
            }
            .padding(8.dp) // внутренний padding
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = result.owner.avatarUrl,
                    contentDescription = "Аватар владельца",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RectangleShape)
                        .clickable {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(result.owner.profileUrl))
                            context.startActivity(intent)
                        }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = result.owner.login,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.owner.profileUrl))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_star_border_24),
                        contentDescription = "stars",
                        tint = Color.Gray
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = result.stars.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_people_outline_24),
                        contentDescription = "watchers",
                        tint = Color.Gray
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = result.watchers.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_fork_right_24),
                        contentDescription = "dorks",
                        tint = Color.Gray
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = result.forks.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = result.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (expanded) "Свернуть" else "Подробнее",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(vertical = 4.dp)
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Автор: ${result.owner.login}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Создан: ${formatDate(result.createdAt)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Обновлен: ${formatDate(result.updatedAt)}",
                    style = MaterialTheme.typography.bodySmall
                )
                result.description?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Описание: $it")
                }
            }
        }
    }
}
