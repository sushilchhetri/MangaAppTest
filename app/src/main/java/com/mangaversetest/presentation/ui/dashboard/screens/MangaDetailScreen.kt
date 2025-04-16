package com.mangaversetest.presentation.ui.dashboard.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mangaversetest.domain.ManageModel

@Composable
fun MangaDetailScreen(mangaModel: ManageModel) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)

    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = mangaModel.thumb,
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            // Status badge
            Text(
                text = mangaModel.status?:"N/A",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .background(
                        color = if (mangaModel.status!!.lowercase() == "ongoing") Color(0xFF4CAF50) else Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .align(Alignment.TopStart)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mangaModel.title?:"N/A",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            mangaModel.subTitle?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Summary",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            mangaModel.summary?.let {summary->
                Text(
                    text = summary.trim(),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Genres",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            mangaModel.genres?.let { genres ->
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    genres.forEach { genre ->
                        Text(
                            text = genre,
                            color = Color.White,
                            modifier = Modifier
                                .padding(end = 8.dp) // spacing between tags
                                .background(
                                    color = Color(0xFF6200EE),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.height(20.dp))

            mangaModel.type?.let {type->
                Text(
                    text = "Type: ${type.replaceFirstChar { it.uppercase() }}",
                    fontSize = 14.sp
                )
            }

            Text(
                text = "Chapters: ${mangaModel.totalChapter}",
                fontSize = 14.sp
            )

        }
    }
}

