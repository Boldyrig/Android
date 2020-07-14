package com.gmail.fuskerr63.android.library.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.NonNull;

@SuppressWarnings("ALL")
public class GeoCodeResponse {
    @NonNull
    @SerializedName("response")
    private  GeoResponse response;

    @NonNull
    public GeoResponse getResponse() {
        return response;
    }

    public static class GeoResponse {
        @NonNull
        @SerializedName("GeoObjectCollection")
        private  GeoObjectCollection geoObjectCollection;

        @NonNull
        public GeoObjectCollection getGeoObjectCollection() {
            return geoObjectCollection;
        }

        public static class GeoObjectCollection {
            @NonNull
            @SerializedName("featureMember")
            private List<FeatureMember> featureMember;

            @NonNull
            public List<FeatureMember> getFeatureMember() {
                return featureMember;
            }

            public static class FeatureMember {
                @NonNull
                @SerializedName("GeoObject")
                private GeoObject geoObject;

                @NonNull
                public GeoObject getGeoObject() {
                    return geoObject;
                }

                public static class GeoObject {
                    @NonNull
                    @SerializedName("metaDataProperty")
                    private MetaDataProperty metaDataProperty;

                    @NonNull
                    public MetaDataProperty getMetaDataProperty() {
                        return metaDataProperty;
                    }

                    public static class MetaDataProperty {
                        @NonNull
                        @SerializedName("GeocoderMetaData")
                        private GeocoderMetaData geocoderMetaData;

                        @NonNull
                        public GeocoderMetaData getGeocoderMetaData() {
                            return geocoderMetaData;
                        }

                        public static class GeocoderMetaData {
                            @NonNull
                            @SerializedName("text")
                            private String text;

                            public void setText(String text) {
                                this.text = text;
                            }

                            @NonNull
                            public String getText() {
                                return text;
                            }
                        }
                    }
                }
            }
        }
    }
}
