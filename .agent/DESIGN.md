# Design System Strategy: Onyx & Emerald Precision

## 1. Overview & Creative North Star: "The Obsidian Ledger"
This design system moves away from the "neon-on-black" cliché of amateur fintech apps toward a "The Obsidian Ledger" aesthetic. The goal is to create a digital environment that feels like a private banking lounge—quiet, authoritative, and meticulously organized.

The system rejects the standard "flat" container approach. Instead, we utilize **Tonal Architecture** and **Intentional Asymmetry**. We break the template look by using generous white space (negative space) and overlapping elements where a primary action might "bridge" two surface tiers, creating a sense of physical assembly rather than a flat digital screen.

## 2. Color & Atmospheric Depth
The palette is rooted in a deep neutral base (#111412) to provide a softer, more sophisticated dark mode than pure black.

### The "No-Line" Rule
**Borders are prohibited for structural sectioning.** We do not use 1px lines to separate content. Boundaries must be defined exclusively through background shifts:
*   Place a `surface-container-low` card against a `surface` background.
*   Use `surface-container-highest` only for the most critical interactive elements (like an active navigation rail or a focused input).

### Surface Hierarchy & Nesting
Treat the UI as a series of nested physical layers.
*   **Base Layer:** `surface` (#111412)
*   **Sectioning:** `surface-container-low`
*   **Interactive Cards:** `surface-container`
*   **Elevated Modals:** `surface-container-high`

### The "Glass & Gradient" Rule
To inject "soul" into the financial data, use Glassmorphism for floating navigation bars or quick-action sheets. Apply a `backdrop-filter: blur(20px)` with a semi-transparent `surface-variant` at 60% opacity.
*   **Signature Textures:** Main CTAs should not be flat. Use a subtle linear gradient from the primary emerald (#00E475) to a deeper container shade at a 135-degree angle to create a "jewel" effect.

## 3. Typography: Editorial Authority
We use **Manrope** exclusively for headlines, body text, and labels. Its geometric yet slightly condensed nature provides a technical, high-end feel perfect for numerical data.

*   **Display (Large/Medium):** Reserved for portfolio balances. Use `on-surface` with a `letter-spacing: -0.02em` to feel tighter and more premium.
*   **Headline (Small):** Use for section headers. Always paired with a primary (#00E475) accent icon or a small decorative "dot" to anchor the eye.
*   **Body-LG/MD:** The workhorse for financial data. Use `on-surface-variant` for secondary descriptions to reduce visual noise.
*   **Labels:** Use `label-sm` in all-caps with `0.05em` tracking for metadata (e.g., "TRANSACTION DATE") to create an archival, ledger-like appearance.

## 4. Elevation & Depth: Tonal Layering
Traditional shadows are often too "muddy" for a dark UI. We use light and tone to define height.

*   **The Layering Principle:** Depth is achieved by stacking. A `surface-container-lowest` card placed on a `surface-container-low` section creates a "recessed" look, perfect for historical data or logs.
*   **Ambient Shadows:** If an element must float (like a FAB), use a large 32px blur with 4% opacity, using the primary emerald color as the shadow tint rather than black. This creates a subtle "glow" rather than a heavy shadow.
*   **The Ghost Border Fallback:** If accessibility requires a border, use a subtle outline at **15% opacity**. It should be felt, not seen.

## 5. Components & Interactive Patterns

### Buttons
*   **Primary:** High roundedness/pill-shaped (24px). Gradient fill (Primary to Primary-Container). Text color: `on-primary`.
*   **Secondary:** Ghost style. No fill, using a muted secondary emerald (#83D7B2) outline at 20% opacity. Text color: `primary` (#00E475).
*   **Tertiary:** Text only, `on-surface` color, with a `primary` underline on hover.

### Input Fields
*   **Styling:** Use `surface-container-highest`. No bottom line. Use a maximum corner radius for a modern, pill-like feel.
*   **Focus State:** The border transitions from 0% to 100% opacity of the primary emerald (#00E475) token.

### Cards & Lists
*   **Forbid Dividers:** Use the spacious layout scale (Level 3) to separate list items with ample vertical white space.
*   **Selection:** Active list items should shift to a brighter surface with a `4px` vertical "pill" of primary color on the far left.

### Financial Visualization (Custom Component)
*   **The Emerald Spark:** Line charts should use a primary emerald stroke with a decreasing-opacity gradient fill (Emerald to Transparent) beneath the line to create volume.

## 6. Do's and Don'ts

### Do
*   **Do** use the secondary mint-emerald (#83D7B2) for "read-only" data or labels to keep the vibrant primary green reserved for actions.
*   **Do** allow elements to break the grid (asymmetry) to highlight high-priority financial insights.
*   **Do** use maximum spacing (Level 3) to achieve the editorial, "Obsidian Ledger" feel.

### Don't
*   **Don't** use 100% white (#FFFFFF). Always use the `on-surface` tonal white to prevent eye strain.
*   **Don't** use standard Material 3 "elevated" shadows. They look "dusty" on dark charcoal backgrounds.
*   **Don't** use dividers. If the content feels cluttered, rely on background tone shifts or increased whitespace.