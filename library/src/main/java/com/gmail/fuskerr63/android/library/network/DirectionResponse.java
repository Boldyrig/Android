package com.gmail.fuskerr63.android.library.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("ALL")
public class DirectionResponse {
    @SerializedName("routes")
    private transient List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        @SerializedName("bounds")
        private transient Bound bounds;
        @SerializedName("overview_polyline")
        private transient OverviewPolyline overviewPolyline;

        public Bound getBounds() {
            return bounds;
        }

        public OverviewPolyline getOverviewPolyline() {
            return overviewPolyline;
        }

        public static class Bound {
            @SerializedName("northeast")
            private transient NorthEast northEast;
            @SerializedName("southwest")
            private  transient SouthWest southWest;

            public NorthEast getNorthEast() {
                return northEast;
            }

            public SouthWest getSouthWest() {
                return southWest;
            }

            public static class NorthEast {
                @SerializedName("lat")
                private transient double lat;
                @SerializedName("lng")
                private transient double lng;

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }

            public static class SouthWest {
                @SerializedName("lat")
                private transient double lat;
                @SerializedName("lng")
                private transient double lng;

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }
        }

        public static class OverviewPolyline {
            @SerializedName("points")
            private transient String points;

            public String getPoints() {
                return points;
            }
        }
    }
}
