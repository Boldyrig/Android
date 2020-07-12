package com.gmail.fuskerr63.android.library.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("ALL")
public class GeoCodeResponse {

    @SerializedName("response")
    private  transient GeoResponse response;

    public GeoResponse getResponse() {
        return response;
    }

    public static class GeoResponse {

        @SerializedName("GeoObjectCollection")
        private  transient GeoObjectCollection geoObjectCollection;

        public GeoObjectCollection getGeoObjectCollection() {
            return geoObjectCollection;
        }

        public static class GeoObjectCollection {

            @SerializedName("featureMember")
            private transient List<FeatureMember> featureMember;

            public List<FeatureMember> getFeatureMember() {
                return featureMember;
            }

            public static class FeatureMember {

                @SerializedName("GeoObject")
                private transient GeoObject geoObject;

                public GeoObject getGeoObject() {
                    return geoObject;
                }

                public static class GeoObject {

                    @SerializedName("metaDataProperty")
                    private transient MetaDataProperty metaDataProperty;

                    public MetaDataProperty getMetaDataProperty() {
                        return metaDataProperty;
                    }

                    public static class MetaDataProperty {

                        @SerializedName("GeocoderMetaData")
                        private transient GeocoderMetaData geocoderMetaData;

                        public GeocoderMetaData getGeocoderMetaData() {
                            return geocoderMetaData;
                        }

                        public static class GeocoderMetaData {
                            @SerializedName("text")
                            private transient String text;

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
