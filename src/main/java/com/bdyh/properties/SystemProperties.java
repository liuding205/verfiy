package com.bdyh.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhaochuanzhen
 * @desc
 * @since 15:35 2018/9/19
 */
@ConfigurationProperties(prefix = "system")
public class SystemProperties {

    private Path path = new Path();

    public class Path {
        private String pictureSourceMax;
        private String pictureSourceSm;
        private String pictureOutput;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPictureOutput() {
            return pictureOutput;
        }

        public void setPictureOutput(String pictureOutput) {
            this.pictureOutput = pictureOutput;
        }

        public String getPictureSourceMax() {
            return pictureSourceMax;
        }

        public void setPictureSourceMax(String pictureSourceMax) {
            this.pictureSourceMax = pictureSourceMax;
        }

        public String getPictureSourceSm() {
            return pictureSourceSm;
        }

        public void setPictureSourceSm(String pictureSourceSm) {
            this.pictureSourceSm = pictureSourceSm;
        }
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
