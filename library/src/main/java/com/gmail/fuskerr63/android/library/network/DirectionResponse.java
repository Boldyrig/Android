package com.gmail.fuskerr63.android.library.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResponse {

    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        @SerializedName("bounds")
        private List<Bound> bounds;
        @SerializedName("overview_polyline")
        private OverviewPolyline overviewPolyline;

        public List<Bound> getBounds() {
            return bounds;
        }

        public OverviewPolyline getOverviewPolyline() {
            return overviewPolyline;
        }

        public static class Bound {
            @SerializedName("northeast")
            private NorthEast northEast;
            @SerializedName("southwest")
            private SouthWest southWest;

            public NorthEast getNorthEast() {
                return northEast;
            }

            public SouthWest getSouthWest() {
                return southWest;
            }

            public static class NorthEast {
                @SerializedName("lat")
                private double lat;
                @SerializedName("lng")
                private double lng;

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }

            public static class SouthWest {
                @SerializedName("lat")
                private double lat;
                @SerializedName("lng")
                private double lng;

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
            private String points;

            public String getPoints() {
                return points;
            }
        }
    }
}
