using System;
using System.Collections.Generic;
using System.Text;

namespace WeatherApp.Model
{
    public class City
    {
        public string Name { get; set; }
        public string CountryCode { get; set; }

        public List<City> GetCities()
        {
            List<City> cities = new List<City>()
            {
                new City()
                {
                    Name="Porto",
                    CountryCode="PT"
                },
                new City()
                {
                    Name="Lisboa",
                    CountryCode="PT"
                },
                new City()
                {
                    Name="Braga",
                    CountryCode="PT"
                },
                new City()
                {
                    Name="Faro",
                    CountryCode="PT"
                },
            };
            return cities;
        }
    }
}
