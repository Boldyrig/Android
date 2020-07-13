package com.gmail.fuskerr63.android.library.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class DirectionResponse {
    @NonNull
    @SerializedName("routes")
    private List<Route> routes;

    @NonNull
    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        @NonNull
        @SerializedName("bounds")
        private Bound bounds;
        @NonNull
        @SerializedName("overview_polyline")
        private OverviewPolyline overviewPolyline;

        @NonNull
        public Bound getBounds() {
            return bounds;
        }

        @NonNull
        public OverviewPolyline getOverviewPolyline() {
            return overviewPolyline;
        }

        public static class Bound {
            @NonNull
            @SerializedName("northeast")
            private NorthEast northEast;
            @NonNull
            @SerializedName("southwest")
            private  SouthWest southWest;

            @NonNull
            public NorthEast getNorthEast() {
                return northEast;
            }

            @NonNull
            public SouthWest getSouthWest() {
                return southWest;
            }

            public static class NorthEast {
                @NonNull
                @SerializedName("lat")
                private double lat;
                @NonNull
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
                @NonNull
                @SerializedName("lat")
                private double lat;
                @NonNull
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
            @NonNull
            @SerializedName("points")
            private String points;

            @NonNull
            public String getPoints() {
                return points;
            }
        }
    }
}
