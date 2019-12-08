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

        public Weather CurrentWeather;

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

        public List<City> GetAllCities()
        {
            List<City> cities = new List<City>()
            {
                new City()
                {
                    Name="Aveiro",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Beja",
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
                    Name="Bragança",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="11ºC"
                },
                new City()
                {
                    Name="Castelo Branco",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Coimbra",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="10ºC"
                },
                new City()
                {
                    Name="Évora",
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
                new City()
                {
                    Name="Guarda",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Leiria",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="10ºC"
                },
                new City()
                {
                    Name="Lisboa",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Portalegre",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="11ºC"
                },
                new City()
                {
                    Name="Porto",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Santarém",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="10ºC"
                },
                new City()
                {
                    Name="Setúbal",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                },
                new City()
                {
                    Name="Viana do Castelo",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="11ºC"
                },
                new City()
                {
                    Name="Vila Real",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="10ºC"
                },
                new City()
                {
                    Name="Viseu",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="12ºC"
                }
            };
            return cities;
        }
    }
}
