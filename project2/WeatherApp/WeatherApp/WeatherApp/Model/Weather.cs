using System;
namespace WeatherApp.Model
{
    public class Weather
    {
        public class weather
        {
            public int id;
            public string main;
            public string description;
            public string icon;
        }

        public class main
        {
            public float temp;
            public int pressure;
            public int humidity;
            public int temp_min;
            public int temp_max; 
        }

        public int visibility;

        public class wind
        {
            public float speed;
        }

        public class sys
        {
            public string country;
            public int sunrise;
            public int sunset;
        }

        public int timezone;
        public string name;
    }
}
