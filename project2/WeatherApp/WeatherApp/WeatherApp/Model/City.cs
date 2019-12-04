using System;
using System.Collections.Generic;
using System.Text;

namespace WeatherApp.Model
{
    public class City
    {
        public string Name { get; set; }
        public string CountryCode { get; set; }

        public string CurrentTime { get; set; }

        public string CurrentTemp { get; set; }

        public List<City> GetCities()
        {
            List<City> cities = new List<City>()
            {
                new City()
                {
                    Name="Porto",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Lisboa",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="10ºC"
                },
                new City()
                {
                    Name="Braga",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Faro",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="11ºC"
                },
            };
            return cities;
        }
    }
}
