package com.viewlift.models.data.appcms


data class CountryCodeData(val nameCode: String, val countryCode: String, val countryName: String)

fun getCountries(): List<CountryCodeData> {
    val countries: MutableList<CountryCodeData> = ArrayList<CountryCodeData>()
    countries.add(
        CountryCodeData(
            "au",
            "61",
            "Australia"
        )
    )
    countries.add(
        CountryCodeData(
            "bd",
            "880",
            "Bangladesh"
        )
    )
    countries.add(CountryCodeData("in", "91", "India"))
    countries.add(
        CountryCodeData(
            "gb",
            "44",
            "United Kingdom"
        )
    )
    countries.add(
        CountryCodeData(
            "us",
            "1",
            "United States"
        )
    )
    countries.add(
        CountryCodeData(
            "ad",
            "376",
            "Andorra"
        )
    )
    countries.add(
        CountryCodeData(
            "ae",
            "971",
            "United Arab Emirates (UAE)"
        )
    )
    countries.add(
        CountryCodeData(
            "af",
            "93",
            "Afghanistan"
        )
    )
    countries.add(
        CountryCodeData(
            "ag",
            "1",
            "Antigua and Barbuda"
        )
    )
    countries.add(
        CountryCodeData(
            "ai",
            "1",
            "Anguilla"
        )
    )
    countries.add(
        CountryCodeData(
            "al",
            "355",
            "Albania"
        )
    )
    countries.add(
        CountryCodeData(
            "am",
            "374",
            "Armenia"
        )
    )
    countries.add(
        CountryCodeData(
            "ao",
            "244",
            "Angola"
        )
    )
    countries.add(
        CountryCodeData(
            "aq",
            "672",
            "Antarctica"
        )
    )
    countries.add(
        CountryCodeData(
            "ar",
            "54",
            "Argentina"
        )
    )
    countries.add(
        CountryCodeData(
            "as",
            "1",
            "American Samoa"
        )
    )
    countries.add(
        CountryCodeData(
            "at",
            "43",
            "Austria"
        )
    )
    countries.add(CountryCodeData("aw", "297", "Aruba"))
    countries.add(
        CountryCodeData(
            "ax",
            "358",
            "Åland Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "az",
            "994",
            "Azerbaijan"
        )
    )
    countries.add(
        CountryCodeData(
            "ba",
            "387",
            "Bosnia And Herzegovina"
        )
    )
    countries.add(
        CountryCodeData(
            "bb",
            "1",
            "Barbados"
        )
    )

    countries.add(
        CountryCodeData(
            "be",
            "32",
            "Belgium"
        )
    )
    countries.add(
        CountryCodeData(
            "bf",
            "226",
            "Burkina Faso"
        )
    )
    countries.add(
        CountryCodeData(
            "bg",
            "359",
            "Bulgaria"
        )
    )
    countries.add(
        CountryCodeData(
            "bh",
            "973",
            "Bahrain"
        )
    )
    countries.add(
        CountryCodeData(
            "bi",
            "257",
            "Burundi"
        )
    )
    countries.add(CountryCodeData("bj", "229", "Benin"))
    countries.add(
        CountryCodeData(
            "bl",
            "590",
            "Saint Barthélemy"
        )
    )
    countries.add(CountryCodeData("bm", "1", "Bermuda"))
    countries.add(
        CountryCodeData(
            "bn",
            "673",
            "Brunei Darussalam",
        )
    )
    countries.add(
        CountryCodeData(
            "bo",
            "591",
            "Bolivia, Plurinational State Of",
        )
    )
    countries.add(CountryCodeData("br", "55", "Brazil"))
    countries.add(CountryCodeData("bs", "1", "Bahamas"))
    countries.add(
        CountryCodeData(
            "bt",
            "975",
            "Bhutan",
        )
    )
    countries.add(
        CountryCodeData(
            "bw",
            "267",
            "Botswana"
        )
    )
    countries.add(
        CountryCodeData(
            "by",
            "375",
            "Belarus"
        )
    )
    countries.add(
        CountryCodeData(
            "bz",
            "501",
            "Belize"
        )
    )
    countries.add(CountryCodeData("ca", "1", "Canada"))
    countries.add(
        CountryCodeData(
            "cc",
            "61",
            "Cocos (keeling) Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "cd",
            "243",
            "Congo, The Democratic Republic Of The"
        )
    )
    countries.add(
        CountryCodeData(
            "cf",
            "236",
            "Central African Republic"
        )
    )
    countries.add(CountryCodeData("cg", "242", "Congo"))
    countries.add(
        CountryCodeData(
            "ch",
            "41",
            "Switzerland"
        )
    )
    countries.add(
        CountryCodeData(
            "ci",
            "225",
            "Côte D'ivoire"
        )
    )
    countries.add(
        CountryCodeData(
            "ck",
            "682",
            "Cook Islands"
        )
    )
    countries.add(CountryCodeData("cl", "56", "Chile"))
    countries.add(
        CountryCodeData(
            "cm",
            "237",
            "Cameroon"
        )
    )
    countries.add(CountryCodeData("cn", "86", "China"))
    countries.add(
        CountryCodeData(
            "co",
            "57",
            "Colombia"
        )
    )
    countries.add(
        CountryCodeData(
            "cr",
            "506",
            "Costa Rica"
        )
    )
    countries.add(CountryCodeData("cu", "53", "Cuba"))
    countries.add(
        CountryCodeData(
            "cv",
            "238",
            "Cape Verde"
        )
    )
    countries.add(
        CountryCodeData(
            "cw",
            "599",
            "Curaçao"
        )
    )
    countries.add(
        CountryCodeData(
            "cx",
            "61",
            "Christmas Island"
        )
    )
    countries.add(
        CountryCodeData(
            "cy",
            "357",
            "Cyprus"
        )
    )
    countries.add(
        CountryCodeData(
            "cz",
            "420",
            "Czech Republic"
        )
    )
    countries.add(
        CountryCodeData(
            "de",
            "49",
            "Germany"
        )
    )
    countries.add(
        CountryCodeData(
            "dj",
            "253",
            "Djibouti"
        )
    )
    countries.add(
        CountryCodeData(
            "dk",
            "45",
            "Denmark"
        )
    )
    countries.add(
        CountryCodeData(
            "dm",
            "1",
            "Dominica"
        )
    )
    countries.add(
        CountryCodeData(
            "do",
            "1",
            "Dominican Republic"
        )
    )
    countries.add(
        CountryCodeData(
            "dz",
            "213",
            "Algeria"
        )
    )
    countries.add(
        CountryCodeData(
            "ec",
            "593",
            "Ecuador"
        )
    )
    countries.add(
        CountryCodeData(
            "ee",
            "372",
            "Estonia"
        )
    )
    countries.add(CountryCodeData("eg", "20", "Egypt"))
    countries.add(
        CountryCodeData(
            "er",
            "291",
            "Eritrea"
        )
    )
    countries.add(CountryCodeData("es", "34", "Spain"))
    countries.add(
        CountryCodeData(
            "et",
            "251",
            "Ethiopia"
        )
    )
    countries.add(
        CountryCodeData(
            "fi",
            "358",
            "Finland"
        )
    )
    countries.add(CountryCodeData("fj", "679", "Fiji"))
    countries.add(
        CountryCodeData(
            "fk",
            "500",
            "Falkland Islands (malvinas)"
        )
    )
    countries.add(
        CountryCodeData(
            "fm",
            "691",
            "Micronesia, Federated States Of"
        )
    )
    countries.add(
        CountryCodeData(
            "fo",
            "298",
            "Faroe Islands"
        )
    )
    countries.add(CountryCodeData("fr", "33", "France"))
    countries.add(CountryCodeData("ga", "241", "Gabon"))
    countries.add(CountryCodeData("gd", "1", "Grenada"))
    countries.add(
        CountryCodeData(
            "ge",
            "995",
            "Georgia"
        )
    )
    countries.add(
        CountryCodeData(
            "gf",
            "594",
            "French Guyana"
        )
    )
    countries.add(CountryCodeData("gh", "233", "Ghana"))
    countries.add(
        CountryCodeData(
            "gi",
            "350",
            "Gibraltar"
        )
    )
    countries.add(
        CountryCodeData(
            "gl",
            "299",
            "Greenland"
        )
    )
    countries.add(
        CountryCodeData(
            "gm",
            "220",
            "Gambia"
        )
    )
    countries.add(
        CountryCodeData(
            "gn",
            "224",
            "Guinea"
        )
    )
    countries.add(
        CountryCodeData(
            "gp",
            "450",
            "Guadeloupe"
        )
    )
    countries.add(
        CountryCodeData(
            "gq",
            "240",
            "Equatorial Guinea"
        )
    )
    countries.add(CountryCodeData("gr", "30", "Greece"))
    countries.add(
        CountryCodeData(
            "gt",
            "502",
            "Guatemala"
        )
    )
    countries.add(CountryCodeData("gu", "1", "Guam"))
    countries.add(
        CountryCodeData(
            "gw",
            "245",
            "Guinea-bissau"
        )
    )
    countries.add(
        CountryCodeData(
            "gy",
            "592",
            "Guyana"
        )
    )
    countries.add(
        CountryCodeData(
            "hk",
            "852",
            "Hong Kong"
        )
    )
    countries.add(
        CountryCodeData(
            "hn",
            "504",
            "Honduras"
        )
    )
    countries.add(
        CountryCodeData(
            "hr",
            "385",
            "Croatia"
        )
    )
    countries.add(CountryCodeData("ht", "509", "Haiti"))
    countries.add(
        CountryCodeData(
            "hu",
            "36",
            "Hungary"
        )
    )
    countries.add(
        CountryCodeData(
            "id",
            "62",
            "Indonesia"
        )
    )
    countries.add(
        CountryCodeData(
            "ie",
            "353",
            "Ireland"
        )
    )
    countries.add(
        CountryCodeData(
            "il",
            "972",
            "Israel"
        )
    )
    countries.add(
        CountryCodeData(
            "im",
            "44",
            "Isle Of Man"
        )
    )
    countries.add(
        CountryCodeData(
            "is",
            "354",
            "Iceland"
        )
    )
    countries.add(
        CountryCodeData(
            "io",
            "246",
            "British Indian Ocean Territory"
        )
    )
    countries.add(CountryCodeData("iq", "964", "Iraq"))
    countries.add(
        CountryCodeData(
            "ir",
            "98",
            "Iran, Islamic Republic Of"
        )
    )
    countries.add(CountryCodeData("it", "39", "Italy"))
    countries.add(
        CountryCodeData(
            "je",
            "44",
            "Jersey "
        )
    )
    countries.add(CountryCodeData("jm", "1", "Jamaica"))
    countries.add(
        CountryCodeData(
            "jo",
            "962",
            "Jordan"
        )
    )
    countries.add(CountryCodeData("jp", "81", "Japan"))
    countries.add(CountryCodeData("ke", "254", "Kenya"))
    countries.add(
        CountryCodeData(
            "kg",
            "996",
            "Kyrgyzstan"
        )
    )
    countries.add(
        CountryCodeData(
            "kh",
            "855",
            "Cambodia"
        )
    )
    countries.add(
        CountryCodeData(
            "ki",
            "686",
            "Kiribati"
        )
    )
    countries.add(
        CountryCodeData(
            "km",
            "269",
            "Comoros"
        )
    )
    countries.add(
        CountryCodeData(
            "kn",
            "1",
            "Saint Kitts and Nevis"
        )
    )
    countries.add(
        CountryCodeData(
            "kp",
            "850",
            "North Korea"
        )
    )
    countries.add(
        CountryCodeData(
            "kr",
            "82",
            "South Korea"
        )
    )
    countries.add(
        CountryCodeData(
            "kw",
            "965",
            "Kuwait"
        )
    )
    countries.add(
        CountryCodeData(
            "ky",
            "1",
            "Cayman Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "kz",
            "7",
            "Kazakhstan"
        )
    )
    countries.add(
        CountryCodeData(
            "la",
            "856",
            "Lao People's Democratic Republic"
        )
    )
    countries.add(
        CountryCodeData(
            "lb",
            "961",
            "Lebanon"
        )
    )
    countries.add(
        CountryCodeData(
            "lc",
            "1",
            "Saint Lucia"
        )
    )
    countries.add(
        CountryCodeData(
            "li",
            "423",
            "Liechtenstein"
        )
    )
    countries.add(
        CountryCodeData(
            "lk",
            "94",
            "Sri Lanka"
        )
    )
    countries.add(
        CountryCodeData(
            "lr",
            "231",
            "Liberia"
        )
    )
    countries.add(
        CountryCodeData(
            "ls",
            "266",
            "Lesotho"
        )
    )
    countries.add(
        CountryCodeData(
            "lt",
            "370",
            "Lithuania"
        )
    )
    countries.add(
        CountryCodeData(
            "lu",
            "352",
            "Luxembourg"
        )
    )
    countries.add(
        CountryCodeData(
            "lv",
            "371",
            "Latvia"
        )
    )
    countries.add(CountryCodeData("ly", "218", "Libya"))
    countries.add(
        CountryCodeData(
            "ma",
            "212",
            "Morocco"
        )
    )
    countries.add(
        CountryCodeData(
            "mc",
            "377",
            "Monaco"
        )
    )
    countries.add(
        CountryCodeData(
            "md",
            "373",
            "Moldova, Republic Of"
        )
    )
    countries.add(
        CountryCodeData(
            "me",
            "382",
            "Montenegro"
        )
    )
    countries.add(
        CountryCodeData(
            "mf",
            "590",
            "Saint Martin"
        )
    )
    countries.add(
        CountryCodeData(
            "mg",
            "261",
            "Madagascar"
        )
    )
    countries.add(
        CountryCodeData(
            "mh",
            "692",
            "Marshall Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "mk",
            "389",
            "Macedonia (FYROM)"
        )
    )
    countries.add(CountryCodeData("ml", "223", "Mali"))
    countries.add(
        CountryCodeData(
            "mm",
            "95",
            "Myanmar"
        )
    )
    countries.add(
        CountryCodeData(
            "mn",
            "976",
            "Mongolia"
        )
    )
    countries.add(CountryCodeData("mo", "853", "Macau"))
    countries.add(
        CountryCodeData(
            "mp",
            "1",
            "Northern Mariana Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "mq",
            "596",
            "Martinique"
        )
    )
    countries.add(
        CountryCodeData(
            "mr",
            "222",
            "Mauritania"
        )
    )
    countries.add(
        CountryCodeData(
            "ms",
            "1",
            "Montserrat"
        )
    )
    countries.add(CountryCodeData("mt", "356", "Malta"))
    countries.add(
        CountryCodeData(
            "mu",
            "230",
            "Mauritius"
        )
    )
    countries.add(
        CountryCodeData(
            "mv",
            "960",
            "Maldives"
        )
    )
    countries.add(
        CountryCodeData(
            "mw",
            "265",
            "Malawi"
        )
    )
    countries.add(CountryCodeData("mx", "52", "Mexico"))
    countries.add(
        CountryCodeData(
            "my",
            "60",
            "Malaysia"
        )
    )
    countries.add(
        CountryCodeData(
            "mz",
            "258",
            "Mozambique"
        )
    )
    countries.add(
        CountryCodeData(
            "na",
            "264",
            "Namibia"
        )
    )
    countries.add(
        CountryCodeData(
            "nc",
            "687",
            "New Caledonia"
        )
    )
    countries.add(CountryCodeData("ne", "227", "Niger"))
    countries.add(
        CountryCodeData(
            "nf",
            "672",
            "Norfolk Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "ng",
            "234",
            "Nigeria"
        )
    )
    countries.add(
        CountryCodeData(
            "ni",
            "505",
            "Nicaragua"
        )
    )
    countries.add(
        CountryCodeData(
            "nl",
            "31",
            "Netherlands"
        )
    )
    countries.add(CountryCodeData("no", "47", "Norway"))
    countries.add(CountryCodeData("np", "977", "Nepal"))
    countries.add(CountryCodeData("nr", "674", "Nauru"))
    countries.add(CountryCodeData("nu", "683", "Niue"))
    countries.add(
        CountryCodeData(
            "nz",
            "64",
            "New Zealand"
        )
    )
    countries.add(CountryCodeData("om", "968", "Oman"))
    countries.add(
        CountryCodeData(
            "pa",
            "507",
            "Panama"
        )
    )
    countries.add(CountryCodeData("pe", "51", "Peru"))
    countries.add(
        CountryCodeData(
            "pf",
            "689",
            "French Polynesia"
        )
    )
    countries.add(
        CountryCodeData(
            "pg",
            "675",
            "Papua New Guinea"
        )
    )
    countries.add(
        CountryCodeData(
            "ph",
            "63",
            "Philippines"
        )
    )
    countries.add(
        CountryCodeData(
            "pk",
            "92",
            "Pakistan"
        )
    )
    countries.add(CountryCodeData("pl", "48", "Poland"))
    countries.add(
        CountryCodeData(
            "pm",
            "508",
            "Saint Pierre And Miquelon"
        )
    )
    countries.add(
        CountryCodeData(
            "pn",
            "870",
            "Pitcairn Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "pr",
            "1",
            "Puerto Rico"
        )
    )
    countries.add(
        CountryCodeData(
            "ps",
            "970",
            "Palestine"
        )
    )
    countries.add(
        CountryCodeData(
            "pt",
            "351",
            "Portugal"
        )
    )
    countries.add(CountryCodeData("pw", "680", "Palau"))
    countries.add(
        CountryCodeData(
            "py",
            "595",
            "Paraguay"
        )
    )
    countries.add(CountryCodeData("qa", "974", "Qatar"))
    countries.add(
        CountryCodeData(
            "re",
            "262",
            "Réunion"
        )
    )
    countries.add(
        CountryCodeData(
            "ro",
            "40",
            "Romania"
        )
    )
    countries.add(
        CountryCodeData(
            "rs",
            "381",
            "Serbia"
        )
    )
    countries.add(
        CountryCodeData(
            "ru",
            "7",
            "Russian Federation"
        )
    )
    countries.add(
        CountryCodeData(
            "rw",
            "250",
            "Rwanda"
        )
    )
    countries.add(
        CountryCodeData(
            "sa",
            "966",
            "Saudi Arabia"
        )
    )
    countries.add(
        CountryCodeData(
            "sb",
            "677",
            "Solomon Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "sc",
            "248",
            "Seychelles"
        )
    )
    countries.add(CountryCodeData("sd", "249", "Sudan"))
    countries.add(CountryCodeData("se", "46", "Sweden"))
    countries.add(
        CountryCodeData(
            "sg",
            "65",
            "Singapore"
        )
    )
    countries.add(
        CountryCodeData(
            "sh",
            "290",
            "Saint Helena, Ascension And Tristan Da Cunha"
        )
    )
    countries.add(
        CountryCodeData(
            "si",
            "386",
            "Slovenia"
        )
    )
    countries.add(
        CountryCodeData(
            "sk",
            "421",
            "Slovakia"
        )
    )
    countries.add(
        CountryCodeData(
            "sl",
            "232",
            "Sierra Leone"
        )
    )
    countries.add(
        CountryCodeData(
            "sm",
            "378",
            "San Marino"
        )
    )
    countries.add(
        CountryCodeData(
            "sn",
            "221",
            "Senegal"
        )
    )
    countries.add(
        CountryCodeData(
            "so",
            "252",
            "Somalia"
        )
    )
    countries.add(
        CountryCodeData(
            "sr",
            "597",
            "Suriname"
        )
    )
    countries.add(
        CountryCodeData(
            "ss",
            "211",
            "South Sudan"
        )
    )
    countries.add(
        CountryCodeData(
            "st",
            "239",
            "Sao Tome And Principe"
        )
    )
    countries.add(
        CountryCodeData(
            "sv",
            "503",
            "El Salvador"
        )
    )
    countries.add(
        CountryCodeData(
            "sx",
            "1",
            "Sint Maarten"
        )
    )
    countries.add(
        CountryCodeData(
            "sy",
            "963",
            "Syrian Arab Republic"
        )
    )
    countries.add(
        CountryCodeData(
            "sz",
            "268",
            "Swaziland"
        )
    )
    countries.add(
        CountryCodeData(
            "tc",
            "1",
            "Turks and Caicos Islands"
        )
    )
    countries.add(CountryCodeData("td", "235", "Chad"))
    countries.add(CountryCodeData("tg", "228", "Togo"))
    countries.add(
        CountryCodeData(
            "th",
            "66",
            "Thailand"
        )
    )
    countries.add(
        CountryCodeData(
            "tj",
            "992",
            "Tajikistan"
        )
    )
    countries.add(
        CountryCodeData(
            "tk",
            "690",
            "Tokelau"
        )
    )
    countries.add(
        CountryCodeData(
            "tl",
            "670",
            "Timor-leste"
        )
    )
    countries.add(
        CountryCodeData(
            "tm",
            "993",
            "Turkmenistan"
        )
    )
    countries.add(
        CountryCodeData(
            "tn",
            "216",
            "Tunisia"
        )
    )
    countries.add(CountryCodeData("to", "676", "Tonga"))
    countries.add(CountryCodeData("tr", "90", "Turkey"))
    countries.add(
        CountryCodeData(
            "tt",
            "1",
            "Trinidad &amp; Tobago"
        )
    )
    countries.add(
        CountryCodeData(
            "tv",
            "688",
            "Tuvalu"
        )
    )
    countries.add(
        CountryCodeData(
            "tw",
            "886",
            "Taiwan"
        )
    )
    countries.add(
        CountryCodeData(
            "tz",
            "255",
            "Tanzania, United Republic Of"
        )
    )
    countries.add(
        CountryCodeData(
            "ua",
            "380",
            "Ukraine"
        )
    )
    countries.add(
        CountryCodeData(
            "ug",
            "256",
            "Uganda"
        )
    )
    countries.add(
        CountryCodeData(
            "uy",
            "598",
            "Uruguay"
        )
    )
    countries.add(
        CountryCodeData(
            "uz",
            "998",
            "Uzbekistan"
        )
    )
    countries.add(
        CountryCodeData(
            "va",
            "379",
            "Holy See (vatican City State)"
        )
    )
    countries.add(
        CountryCodeData(
            "vc",
            "1",
            "Saint Vincent &amp; The Grenadines"
        )
    )
    countries.add(
        CountryCodeData(
            "ve",
            "58",
            "Venezuela, Bolivarian Republic Of"
        )
    )
    countries.add(
        CountryCodeData(
            "vg",
            "1",
            "British Virgin Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "vi",
            "1",
            "US Virgin Islands"
        )
    )
    countries.add(
        CountryCodeData(
            "vn",
            "84",
            "Vietnam"
        )
    )
    countries.add(
        CountryCodeData(
            "vu",
            "678",
            "Vanuatu"
        )
    )
    countries.add(
        CountryCodeData(
            "wf",
            "681",
            "Wallis And Futuna"
        )
    )
    countries.add(CountryCodeData("ws", "685", "Samoa"))
    countries.add(
        CountryCodeData(
            "xk",
            "383",
            "Kosovo"
        )
    )
    countries.add(CountryCodeData("ye", "967", "Yemen"))
    countries.add(
        CountryCodeData(
            "yt",
            "262",
            "Mayotte"
        )
    )
    countries.add(
        CountryCodeData(
            "za",
            "27",
            "South Africa"
        )
    )
    countries.add(
        CountryCodeData(
            "zm",
            "260",
            "Zambia"
        )
    )
    countries.add(
        CountryCodeData(
            "zw",
            "263",
            "Zimbabwe"
        )
    )
    return countries
}