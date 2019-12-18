using System;
using System.Collections.Generic;

namespace WeatherApp.Model
{
    public class Main
    {
        public float temp;
        public float temp_min;
        public float temp_max;
        public float pressure;
        public float sea_level;
        public float grnd_level;
        public float humidity;
        public float temp_kf;
    }

    public class forecastInfo
    {
        public string main;
        public string description;
        public string icon;
    }

    public class forecastWind
    {
        public float speed;
        public float deg;
    }

    public class Entry
    {
        // Datetime
        public int dt;
        public Main main;
        public IList<forecastInfo> weather;
        public forecastWind wind;
        public Dictionary<string, float> rain;
        // Datetime as a string
        public string dt_txt;
    }

    public class Info
    {
        public int id;
        public string name;
        public int population;
        public int timezone;
        public int sunrise;
        public int sunset;
    }

    public class Forecast
    {
        // Entry array size
        public int cnt;
        public IList<Entry> list;
        public Info city;
    }
}
