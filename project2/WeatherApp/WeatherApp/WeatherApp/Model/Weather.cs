using System;
namespace WeatherApp.Model
{
    public class Weather
    {
        public class info
        {
            public int id;
            public string main;
            public string description;
            public string icon;
        }

        public class mainStats
        {
            public float temp;
            public int pressure;
            public int humidity;
            public int temp_min;
            public int temp_max;
        }

        public class Wind
        {
            public float speed;
        }

        public class Sys
        {
            public string country;
            public int sunrise;
            public int sunset;
        }

        public info weather;
        public mainStats main;
        public int visibility;
        public Wind wind;
        public Sys sys;
        public int timezone;
        public string name;
    }
}
