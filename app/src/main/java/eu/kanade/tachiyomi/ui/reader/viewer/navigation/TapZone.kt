package eu.kanade.tachiyomi.ui.reader.viewer.navigation

import android.graphics.RectF

data class TapZoneRegion(
    // Bounds in relative coordinates (0..1)
    val bounds: RectF,
    val type: RegionType
) {
    enum class RegionType {
        LEFT,
        RIGHT
    }
}

class TapZone(val regions: List<TapZoneRegion>) {
    fun getRegion(x: Float, y: Float): TapZoneRegion? {
        return regions.firstOrNull {
            it.bounds.left <= x && x <= it.bounds.right && it.bounds.top <= y && y <= it.bounds.bottom
        }
    }

    companion object {
        val leftRight = TapZone(
            listOf(
                TapZoneRegion(RectF(0f, 0f, 1f / 3f, 1f), TapZoneRegion.RegionType.LEFT),
                TapZoneRegion(RectF(2f / 3f, 0f, 1f, 1f), TapZoneRegion.RegionType.RIGHT)
            )
        )

        val lShaped = TapZone(
            listOf(
                TapZoneRegion(RectF(0f, 1f / 3f, 1f / 3f, 2f / 3f), TapZoneRegion.RegionType.LEFT),
                TapZoneRegion(RectF(0f, 0f, 1f, 1f / 3f), TapZoneRegion.RegionType.LEFT),
                TapZoneRegion(RectF(2f / 3f, 1f / 3f, 1f, 1f), TapZoneRegion.RegionType.RIGHT),
                TapZoneRegion(RectF(0f, 2f / 3f, 2f / 3f, 1f), TapZoneRegion.RegionType.RIGHT)
            )
        )

        val kindle = TapZone(
            listOf(
                TapZoneRegion(RectF(0f, 1f / 3f, 1f / 3f, 1f), TapZoneRegion.RegionType.LEFT),
                TapZoneRegion(RectF(1f / 3f, 1f / 3f, 1f, 1f), TapZoneRegion.RegionType.RIGHT)
            )
        )

        val edge = TapZone(
            listOf(
                TapZoneRegion(RectF(0f, 0f, 1f / 3f, 1f), TapZoneRegion.RegionType.RIGHT),
                TapZoneRegion(RectF(1f / 3f, 2f / 3f, 2f / 3f, 1f), TapZoneRegion.RegionType.LEFT),
                TapZoneRegion(RectF(2f / 3f, 0f, 1f, 1f), TapZoneRegion.RegionType.RIGHT)
            )
        )
    }
}

enum class TapZones(val value: Int) {
    AUTOMATIC(0),
    LEFT_RIGHT(1),
    L_SHAPED(2),
    KINDLE(3),
    EDGE(4),
    DISABLED(5);

    fun getTapZone(isPager: Boolean = true): TapZone? {
        return when (this) {
            AUTOMATIC -> if (isPager) TapZone.leftRight else TapZone.lShaped
            LEFT_RIGHT -> TapZone.leftRight
            L_SHAPED -> TapZone.lShaped
            KINDLE -> TapZone.kindle
            EDGE -> TapZone.edge
            DISABLED -> null
        }
    }

    companion object {
        fun fromPreference(value: Int): TapZones {
            return values().firstOrNull { it.value == value } ?: AUTOMATIC
        }
    }
}
