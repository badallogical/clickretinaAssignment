package com.example.clickassignment

import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(35)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text(
                    text = "Android Assignment",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Red // Red background for the app bar
            ) )
                 },
        content = { paddingValues ->
            AssignmentUI(uiState, modifier = Modifier.padding(paddingValues)) // Your main UI content
        }
    )

}




@RequiresApi(35)
@Composable
fun AssignmentUI(uiState: ScreenUiState, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OverallScoreCard()

        Spacer(modifier = Modifier.height(16.dp))

        // Title Section
        val contents = uiState.content
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(contents) { content ->
                ContentCard(
                    title = "TITLE",
                    contentText  = content.titles.joinToString(", ") ?: "No titles available",
                    {},
                    {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description Section
                ContentCard(
                    title = "DESCRIPTION",
                    contentText  =content.description.takeIf { !it.isNullOrBlank() } ?: "No content available"
                    ,{},{}
                )
            }
        }


    }
}

@Composable
fun OverallScoreCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Header text
        Text(
            text = "OVERALL SCORE",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        // Score card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title and Circular Score
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Overall Score",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(alignment = Alignment.Start)
                        )
                        Text(
                            text = "AVERAGE",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.align(alignment = Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))


                    // Score Text Overlay
                    Box(
                        modifier = Modifier
                            .size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        // Background Circle (Unfilled part)
                        CircularProgressIndicator(
                            progress = 1f, // Full circle
                            color = Color(0xFFE0E0E0), // Light Gray for the unfilled background
                            strokeWidth = 10.dp,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Foreground Circle (Filled progress)
                        CircularProgressIndicator(
                            progress = 0.53f, // 53/100
                            color = Color(0xFFFFC107), // Yellow for the progress
                            strokeWidth = 10.dp,
                            modifier = Modifier.fillMaxSize()
                        )

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "53",
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF9800) // Orange Color
                                )
                            )
                            Text(
                                text = "/100",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.Black),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Volume & Competition Row
                Row( modifier = Modifier.fillMaxWidth(), horizontalArrangement =  Arrangement.SpaceBetween){
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Search Volume",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "HIGH",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Red
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Competition",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "HIGH",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContentCard(title: String, contentText: String, onCopyClick: () -> Unit, onShareClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title Text above the card
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Card with content and actions
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation =CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Main Content
                Text(
                    text = contentText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )

                // Divider and Actions
                Divider(color = Color.LightGray, thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Copy Button
                    TextButton(
                        onClick = { onCopyClick() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Copy",
                            color = Color.Blue,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Vertical Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.LightGray)
                    )

                    // Share Button
                    TextButton(
                        onClick = { onShareClick() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Share",
                            color = Color.Blue,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAssignmentUI() {
}
