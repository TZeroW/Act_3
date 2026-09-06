package com.example.act3.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.act3.model.Event
import com.example.act3.ui.EventViewModel
import com.example.act3.ui.theme.ButtonBlue
import com.example.act3.ui.theme.ButtonTextDark
import com.example.act3.ui.theme.DarkBackground
import com.example.act3.ui.theme.DarkBorder
import com.example.act3.ui.theme.DarkSurface
import com.example.act3.ui.theme.PrimaryBlue
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    viewModel: EventViewModel,
    eventId: String? = null,
    onNavigateBack: () -> Unit
) {
    val eventToEdit = remember(eventId) {
        if (eventId != null) viewModel.events.find { it.id == eventId } else null
    }

    var title by remember { mutableStateOf(eventToEdit?.title ?: "") }
    var description by remember { mutableStateOf(eventToEdit?.description ?: "") }
    var date by remember { mutableStateOf(eventToEdit?.date ?: "") }
    var location by remember { mutableStateOf(eventToEdit?.location ?: "") }
    var category by remember { mutableStateOf(eventToEdit?.category ?: "General") }

    var showErrors by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }

    LaunchedEffect(title, description, date, location) {
        isValid = title.isNotBlank() && description.isNotBlank() && date.isNotBlank() && location.isNotBlank()
    }

    // Configuración del DatePickerDialog para selección de fecha
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val categoriesWithIcons = listOf(
        "General" to null,
        "Tecnología" to Icons.Default.Code,
        "Educación" to Icons.Default.School,
        "Competencia" to Icons.Default.EmojiEvents
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (eventToEdit != null) "Editar Evento" else "Nuevo Evento",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        showErrors = true
                        if (isValid) {
                            if (eventToEdit != null) {
                                viewModel.updateEvent(
                                    eventToEdit.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        date = date.trim(),
                                        location = location.trim(),
                                        category = category.trim().ifEmpty { "General" }
                                    )
                                )
                            } else {
                                viewModel.addEvent(
                                    Event(
                                        title = title.trim(),
                                        description = description.trim(),
                                        date = date.trim(),
                                        location = location.trim(),
                                        category = category.trim().ifEmpty { "General" }
                                    )
                                )
                            }
                            onNavigateBack()
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBlue,
                        contentColor = ButtonTextDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (eventToEdit != null) "Guardar Cambios" else "Guardar Evento",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Campo: Título del Evento
            FormFieldCard(
                icon = Icons.Default.TextFields,
                label = "Título del Evento",
                isRequired = true,
                value = title,
                onValueChange = {
                    title = it
                    if (!showErrors) showErrors = true
                },
                placeholder = "ej. Conferencia Anual 2026",
                isError = showErrors && title.isEmpty(),
                errorMessage = "¡Título requerido!",
                trailingContent = {
                    if (title.isNotEmpty()) {
                        IconButton(
                            onClick = { title = "" },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Borrar",
                                tint = Color(0xFF64748B)
                            )
                        }
                    }
                }
            )

            // Campo: Descripción
            FormFieldCard(
                icon = Icons.AutoMirrored.Filled.Notes,
                label = "Descripción",
                value = description,
                onValueChange = {
                    if (it.length <= 500) {
                        description = it
                        if (!showErrors) showErrors = true
                    }
                },
                placeholder = "Añade detalles, agenda o notas relevantes sobre el evento...",
                isError = showErrors && description.isEmpty(),
                errorMessage = "¡Descripción requerida!",
                minLines = 3,
                footerContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Opcional",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "${description.length}/500",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            )

            // Campo: Fecha (Con selección mediante DatePickerDialog)
            FormFieldCard(
                icon = Icons.Default.CalendarToday,
                label = "Fecha (Seleccionar)",
                value = date,
                onValueChange = { },
                readOnly = true,
                onClick = { datePickerDialog.show() },
                placeholder = "Toca para elegir fecha (DD/MM/AAAA)",
                isError = showErrors && date.isEmpty(),
                errorMessage = "¡Fecha requerida!",
                trailingContent = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendario",
                            tint = Color(0xFF70B2FF),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            )

            // Campo: Ubicación
            FormFieldCard(
                icon = Icons.Default.LocationOn,
                label = "Ubicación",
                value = location,
                onValueChange = {
                    location = it
                    if (!showErrors) showErrors = true
                },
                placeholder = "ej. Auditorio Principal o Enlace Virtual",
                isError = showErrors && location.isEmpty(),
                errorMessage = "¡Ubicación requerida!",
                trailingContent = {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Mapa",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            // Campo: Categoría
            FormFieldCard(
                icon = Icons.Default.Widgets,
                label = "Categoría",
                value = category,
                onValueChange = { },
                readOnly = true,
                placeholder = "General",
                trailingContent = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Seleccionar",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(24.dp)
                    )
                }
            )

            // Chips horizontales de categoría
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoriesWithIcons) { (catName, catIcon) ->
                    val isSelected = category == catName
                    FilterChip(
                        selected = isSelected,
                        onClick = { category = catName },
                        label = { Text(catName) },
                        leadingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else if (catIcon != null) {
                                Icon(
                                    imageVector = catIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF161922),
                            labelColor = Color(0xFF9CA3AF),
                            selectedContainerColor = Color(0xFF2C384A),
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = DarkBorder,
                            selectedBorderColor = PrimaryBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormFieldCard(
    icon: ImageVector,
    label: String,
    isRequired: Boolean = false,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    minLines: Int = 1,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isError) Color.Red else DarkBorder
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .then(
                    if (onClick != null) Modifier.clickable { onClick() } else Modifier
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = if (minLines > 1) Alignment.Top else Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier
                        .size(22.dp)
                        .padding(top = if (minLines > 1) 2.dp else 0.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        if (isRequired) {
                            Text(
                                text = " *",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF70B2FF)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            readOnly = readOnly,
                            minLines = minLines,
                            enabled = onClick == null,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = Color.White
                            ),
                            cursorBrush = SolidColor(PrimaryBlue),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (footerContent != null) {
                        footerContent()
                    }
                }

                if (trailingContent != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingContent()
                }
            }
        }

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}
