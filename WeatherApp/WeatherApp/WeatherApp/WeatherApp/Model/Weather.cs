using System;
using System.Collections.Generic;

namespace WeatherApp.Model
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
        public float pressure;
        public float humidity;
        public float temp_min;
        public float temp_max;
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

    public class Weather
    {
        public IList<info> weather;
        public mainStats main;
        public int visibility;
        public Wind wind;
        public Sys sys;
        public Dictionary<string, float> rain;
        public int timezone;
        public string name;
    }
}
