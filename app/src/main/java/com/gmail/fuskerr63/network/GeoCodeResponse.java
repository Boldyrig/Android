package com.gmail.fuskerr63.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoCodeResponse {

    @SerializedName("response")
    private GeoResponse response;

    public GeoResponse getResponse() {
        return response;
    }

    public static class GeoResponse {

        @SerializedName("GeoObjectCollection")
        private GeoObjectCollection geoObjectCollection;

        public GeoObjectCollection getGeoObjectCollection() {
            return geoObjectCollection;
        }

        public static class GeoObjectCollection {

            @SerializedName("featureMember")
            private List<FeatureMember> featureMember;

            public List<FeatureMember> getFeatureMember() {
                return featureMember;
            }

            public static class FeatureMember {

                @SerializedName("GeoObject")
                private GeoObject geoObject;

                public GeoObject getGeoObject() {
                    return geoObject;
                }

                public static class GeoObject {

                    @SerializedName("metaDataProperty")
                    private MetaDataProperty metaDataProperty;

                    public MetaDataProperty getMetaDataProperty() {
                        return metaDataProperty;
                    }

                    public static class MetaDataProperty {

                        @SerializedName("GeocoderMetaData")
                        private GeocoderMetaData geocoderMetaData;

                        public GeocoderMetaData getGeocoderMetaData() {
                            return geocoderMetaData;
                        }

                        public static class GeocoderMetaData {
                            @SerializedName("text")
                            private String text;

                            public void setText(String text) {
                                this.text = text;
                            }

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
