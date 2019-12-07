using System;
namespace WeatherApp.Model
{
    public class CityWeather
    {
        public City city { get; set; }
        public Weather weather { get; set; }

        public CityWeather(City c, Weather w)
        {
            city = c;
            weather = w;
        }
    }
}
