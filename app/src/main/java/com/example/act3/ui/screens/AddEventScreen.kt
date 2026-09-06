package com.example.act3.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.act3.model.CategoryItem
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

    var isDropdownExpanded by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    // DatePicker setup
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
                        val finalTitle = title.trim().ifEmpty { "Sin título" }
                        if (eventToEdit != null) {
                            viewModel.updateEvent(
                                eventToEdit.copy(
                                    title = finalTitle,
                                    description = description.trim(),
                                    date = date.trim(),
                                    location = location.trim(),
                                    category = category.trim().ifEmpty { "General" }
                                )
                            )
                        } else {
                            viewModel.addEvent(
                                Event(
                                    title = finalTitle,
                                    description = description.trim(),
                                    date = date.trim(),
                                    location = location.trim(),
                                    category = category.trim().ifEmpty { "General" }
                                )
                            )
                        }
                        onNavigateBack()
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

            // Campo: Título del Evento (Opcional)
            FormFieldCard(
                icon = Icons.Default.TextFields,
                label = "Título del Evento",
                value = title,
                onValueChange = { title = it },
                placeholder = "ej. Conferencia Anual 2026",
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

            // Campo: Descripción (Opcional)
            FormFieldCard(
                icon = Icons.AutoMirrored.Filled.Notes,
                label = "Descripción",
                value = description,
                onValueChange = {
                    if (it.length <= 500) {
                        description = it
                    }
                },
                placeholder = "Añade detalles, agenda o notas relevantes sobre el evento...",
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

            // Campo: Fecha (Opcional - DatePickerDialog)
            FormFieldCard(
                icon = Icons.Default.CalendarToday,
                label = "Fecha",
                value = date,
                onValueChange = { },
                readOnly = true,
                onClick = { datePickerDialog.show() },
                placeholder = "Toca para elegir fecha (DD/MM/AAAA)",
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

            // Campo: Ubicación (Opcional)
            FormFieldCard(
                icon = Icons.Default.LocationOn,
                label = "Ubicación",
                value = location,
                onValueChange = { location = it },
                placeholder = "ej. Auditorio Principal o Enlace Virtual",
                trailingContent = {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Mapa",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            // Campo: Categoría (Con menú desplegable y botón de eliminar por categoría)
            Box {
                FormFieldCard(
                    icon = Icons.Default.Widgets,
                    label = "Categoría",
                    value = category,
                    onValueChange = { },
                    readOnly = true,
                    onClick = { isDropdownExpanded = true },
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

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color(0xFF1E222D))
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                ) {
                    viewModel.categories.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = cat.getIcon(),
                                            contentDescription = null,
                                            tint = cat.textColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = cat.name, color = Color.White)
                                    }

                                    if (!cat.name.equals("General", ignoreCase = true)) {
                                        IconButton(
                                            onClick = {
                                                viewModel.removeCategory(cat.name)
                                                if (category.equals(cat.name, ignoreCase = true)) {
                                                    category = "General"
                                                }
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar Categoría",
                                                tint = Color(0xFFEF4444),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            },
                            onClick = {
                                category = cat.name
                                isDropdownExpanded = false
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Agregar nueva categoría",
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        onClick = {
                            isDropdownExpanded = false
                            showAddCategoryDialog = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Diálogo para Agregar Nueva Categoría Personalizada
    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onCategoryCreated = { newCat ->
                viewModel.addCategory(newCat)
                category = newCat.name
                showAddCategoryDialog = false
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onCategoryCreated: (CategoryItem) -> Unit
) {
    var name by remember { mutableStateOf("") }

    val iconOptions = listOf(
        "Code" to Icons.Default.Code,
        "School" to Icons.Default.School,
        "Trophy" to Icons.Default.EmojiEvents,
        "Work" to Icons.Default.Work,
        "Music" to Icons.Default.MusicNote,
        "Fitness" to Icons.Default.FitnessCenter,
        "Palette" to Icons.Default.Palette,
        "Flight" to Icons.Default.Flight,
        "Fastfood" to Icons.Default.Fastfood,
        "Star" to Icons.Default.Star
    )

    var selectedIconKey by remember { mutableStateOf("Code") }

    val colorOptions = listOf(
        Pair(0xFF1E3A5FL, 0xFF70B2FFL), // Blue
        Pair(0xFF123B2BL, 0xFF4EE29BL), // Green
        Pair(0xFF3B1E5FL, 0xFFD08EFFL), // Purple
        Pair(0xFF5F3A1EL, 0xFFFFB270L), // Orange
        Pair(0xFF5F1E3BL, 0xFFFF70D0L), // Pink
        Pair(0xFF1E5F5BL, 0xFF70FFFA0L), // Teal
        Pair(0xFF252A36L, 0xFFFFFFFFL)  // Dark Gray
    )

    var selectedColors by remember { mutableStateOf(colorOptions[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF181B26),
        title = {
            Text(
                text = "Nueva Categoría",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la categoría") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = DarkBorder,
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = Color(0xFF9CA3AF),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Seleccionar Icono:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    iconOptions.forEach { (key, icon) ->
                        val isSelected = selectedIconKey == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PrimaryBlue else Color(0xFF252A36))
                                .clickable { selectedIconKey = key }
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Seleccionar Color:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colorOptions.forEach { pair ->
                        val isSelected = selectedColors == pair
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(pair.first))
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColors = pair },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(pair.second),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onCategoryCreated(
                            CategoryItem(
                                name = name.trim(),
                                iconKey = selectedIconKey,
                                bgColorHex = selectedColors.first,
                                textColorHex = selectedColors.second
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF9CA3AF))
            }
        }
    )
}

@Composable
private fun FormFieldCard(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(
                border = BorderStroke(width = 1.dp, color = DarkBorder),
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
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

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
}
