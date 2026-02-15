package dev.artixdev.practice.utils.cuboid;

/**
 * CuboidDirection
 * 
 * Enum representing directions for cuboid operations.
 * Includes cardinal directions, vertical directions, and special directions.
 * 
 * @author RefineDev
 * @since 1.0
 */
public enum CuboidDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN,
    HORIZONTAL,
    VERTICAL,
    BOTH,
    EASY,
    UNKNOWN;
    
    /**
     * Get the opposite direction.
     * 
     * @return the opposite direction
     */
    public CuboidDirection opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case HORIZONTAL:
                return VERTICAL;
            case VERTICAL:
                return HORIZONTAL;
            case BOTH:
                return BOTH;
            case EASY:
                return UNKNOWN; // EASY has no direct opposite
            case UNKNOWN:
            default:
                return UNKNOWN;
        }
    }
}
