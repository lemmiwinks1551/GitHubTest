package com.example.testapp.ui.repositoryScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testapp.R
import com.example.testapp.bytesToHumanReadableSize
import com.example.testapp.models.RepoContent

@Preview(showBackground = true)
@Composable
fun RepoContentItem(
    content: RepoContent = RepoContent(
        "MySchedule",
        "file",
        "https://github.com/lemmiwinks1551/MySchedule/blob/main/.gitignore",
        "338" // Размер в байтах
    )
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                when (content.type) {
                    "dir" -> {
                        // TODO: Добавить переод в папку
                    }

                    "file" -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(content.htmlUrl))
                        context.startActivity(intent)
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (content.type) {
            "dir" -> {
                Icon(
                    painterResource(id = R.drawable.baseline_folder_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            "file" -> {
                Icon(
                    painterResource(id = R.drawable.baseline_insert_drive_file_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = content.name,
            modifier = Modifier.weight(1f)
        )
        if (content.type == "file") {
            Text(
                text = bytesToHumanReadableSize(content.size.trim().toDouble()),
                textAlign = TextAlign.End,
                modifier = Modifier.width(60.dp)
            )
        } else {
            // папка, не выводим её размер
        }
    }
}
