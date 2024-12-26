# UI Components Documentation

## Main Activity (activity_main.xml)

### Storage Usage Card
```xml
<com.google.android.material.card.MaterialCardView>
    <!-- Shows total storage usage with progress indicator -->
    <!-- Updates in real-time as storage changes -->
</com.google.android.material.card.MaterialCardView>
```

### Category Statistics
```xml
<com.github.mikephil.charting.charts.PieChart>
    <!-- Visualizes storage usage by category -->
    <!-- Interactive chart with animations -->
</com.github.mikephil.charting.charts.PieChart>
```

### Quick Actions
```xml
<com.google.android.material.button.MaterialButton>
    <!-- Primary and secondary actions -->
    <!-- Consistent styling with Material Design -->
</com.google.android.material.button.MaterialButton>
```

## Scanning Process (fragment_scanning.xml)

### Progress Indication
```xml
<com.google.android.material.progressindicator.LinearProgressIndicator>
    <!-- Shows scanning progress -->
    <!-- Updates with current operation -->
</com.google.android.material.progressindicator.LinearProgressIndicator>
```

### Status Updates
```xml
<TextView android:id="@+id/scanningStatus">
    <!-- Real-time scanning status -->
    <!-- Current operation details -->
</TextView>
```


## Settings Screen (fragment_settings.xml)

### Excluded Folders
```xml
<androidx.recyclerview.widget.RecyclerView>
    <!-- List of excluded folders -->
    <!-- Swipe-to-remove functionality -->
</androidx.recyclerview.widget.RecyclerView>
```

### Automatic Cleanup
```xml
<com.google.android.material.switchmaterial.SwitchMaterial>
    <!-- Toggle for automatic cleanup -->
    <!-- Schedule configuration -->
</com.google.android.material.switchmaterial.SwitchMaterial>
```

## Common Components

### Top App Bar
```xml
<com.google.android.material.appbar.MaterialToolbar>
    <!-- App navigation -->
    <!-- Settings access -->
</com.google.android.material.appbar.MaterialToolbar>
```

### Loading Overlay
```xml
<FrameLayout android:id="@+id/loadingOverlay">
    <!-- Full-screen loading indicator -->
    <!-- Semi-transparent background -->
</FrameLayout>
```

## Design Guidelines

### Colors
- Primary: Material Blue (#1976D2)
- Secondary: Material Teal (#00796B)
- Background: Light (#FFFFFF)
- Surface: White (#FFFFFF)
- Error: Material Red (#B00020)

### Typography
- Headlines: Roboto Medium
- Body Text: Roboto Regular
- Buttons: Roboto Medium
- Labels: Roboto Regular

### Spacing
- Card Margin: 16dp
- Internal Padding: 16dp
- Element Spacing: 8dp
- List Item Height: 72dp

### Elevation
- Cards: 1dp
- FAB: 6dp
- App Bar: 4dp
- Dialog: 24dp

## Interaction Patterns

### Button States
- Enabled: Full opacity
- Disabled: 38% opacity
- Pressed: Ripple effect
- Loading: Progress indicator

### Progress Indicators
- Determinate: Linear progress
- Indeterminate: Circular progress
- Scanning: Linear progress with status

### Dialogs
- Confirmation: Two buttons
- Information: Single button
- Progress: Optional cancel
- Error: Retry option

### Lists
- Swipe actions
- Long press selection
- Multi-select mode
- Sort options

## Accessibility

### Content Description
- All interactive elements
- Status updates
- Progress indicators
- Icons and buttons

### Touch Targets
- Minimum size: 48x48dp
- Adequate spacing
- Clear feedback

### Color Contrast
- Text: WCAG AA compliant
- Icons: Clear visibility
- Interactive elements: Distinct

## Animation Guidelines

### Transitions
- Duration: 300ms
- Easing: Material standard
- Page transitions: Shared elements
- List changes: Fade through

### Progress
- Smooth updates
- Clear feedback
- Cancel options
- Success/error states

## Error States

### Empty States
- Helpful messages
- Clear actions
- Consistent styling
- Illustration support

### Error Messages
- Clear language
- Action options
- Visual feedback
- Recovery paths

## Responsive Design

### Screen Sizes
- Phone portrait
- Phone landscape
- Tablet layouts
- Adaptive UI

### Orientation Changes
- State preservation
- Layout adaptation
- Progress continuation
- Dialog persistence
