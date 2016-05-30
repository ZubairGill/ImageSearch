package googleImagesSearch;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class Services {
	
	public static LinkedList<String> getImdbData(String movie) {
		final LinkedList<String> dataIMDB = new LinkedList<>();
		dataIMDB.clear();
		
		String url="http://www.omdbapi.com/?i="+movie+"&plot=short&r=json";
				
		try {
			InputStream input = new URL(url).openStream();
			Map<String, String> map = new Gson().fromJson(
					new InputStreamReader(input, "UTF-8"),
					new TypeToken<Map<String, String>>() {
					}.getType());

			dataIMDB.add(map.get("Title"));
			dataIMDB.add(map.get("Year"));
			dataIMDB.add(map.get("Released"));
			dataIMDB.add(map.get("Runtime"));
			dataIMDB.add(map.get("Genre"));
			dataIMDB.add(map.get("Director"));
			dataIMDB.add(map.get("Writer"));
			dataIMDB.add(map.get("Actors"));
			dataIMDB.add(map.get("Plot"));
			dataIMDB.add(map.get("imdbRating"));
			dataIMDB.add(map.get("imdbID"));

			dataIMDB.addFirst("http://www.imdb.com/title/"
					+ map.get("imdbID").toString() + "/");

		} catch (Exception e) {
			System.err.println("The Error is occured while getting data from IMDB..");
			System.out.println(e.getMessage().toString());
		}
		return dataIMDB;
	}

	
	/*
	 * 	This service provides the list of movies depending upon the Genre type passed as input parameter.
	 */
	@RequestMapping(value = "/genredetail", method = RequestMethod.GET)
	public @ResponseBody GenreModel movieListByGenre(String genre) {

		ArrayList<LinkedList<String>> movieList = new ArrayList<LinkedList<String>>();
		
		System.out.println("Request Received on path /genredetail");
		System.out.println(genre);

		movieList.clear();
		String genreCode = getGenreCode(genre);
		
			try {

				InputStream input = new URL(
						"https://api.themoviedb.org/3/discover/movie?with_genres="
								+ genreCode
								+ "&api_key=746bcc0040f68b8af9d569f27443901f")
						.openStream();

				Map<String, Object> response = toMapObject(IOUtils.toString(
						input, "UTF-8"));
				if (response == null) {
					System.out.println("Response is null!!");
				} else {
					List<Map<String, Object>> filmsArray = (ArrayList<Map<String, Object>>) response.get("results");

					for (int i = 0; i < 10; i++) {

						final LinkedList<String> movieInfoList = new LinkedList<String>();

						if (filmsArray.get(i).get("poster_path").toString() == null) {
							movieInfoList.add("No Poster Available");
						} else {
							movieInfoList.add("http://image.tmdb.org/t/p/w300"
									+ filmsArray.get(i).get("poster_path")
											.toString());
						}
						if (filmsArray.get(i).get("title").toString() == null) {
							movieInfoList.add("No Title Available");
						} else {
							movieInfoList.add(filmsArray.get(i).get("title")
									.toString());
						}
						if (filmsArray.get(i).get("release_date").toString() == null) {
							movieInfoList.add("No Release Date Available");
						} else {
							movieInfoList.add(filmsArray.get(i)
									.get("release_date").toString());
						}
						if (filmsArray.get(i).get("overview").toString() == null) {
							movieInfoList.add("No overview Available");
						} else {
							movieInfoList.add(filmsArray.get(i).get("overview")
									.toString());
						}
						movieList.add(movieInfoList);
					}
				}

			} catch (Exception e) {

				System.out.println(e.getMessage().toString() + "Error in genre details service");
			}
		return new GenreModel(movieList);
	}

	/*
	 * 	This service  provides the detail of writers,directors and actors when name is passed as input parameter.
	 */
	@RequestMapping(value = "/persondetail", method = RequestMethod.GET)
	public @ResponseBody PersonDetailModel personDetail(String personName) {
		System.out.println("Request Received on path /persondetail");
		System.out.println(personName);

		final LinkedList<String> actorsInfoList = new LinkedList<String>();
		actorsInfoList.clear();

		List<String> tmdbId = new ArrayList<String>();
		List<String> tmdbDOB = new ArrayList<String>();

		String personCode = personResource(toTrim(personName));
		try {
			InputStream input = new URL("http://imdb.wemakesites.net/api/"
					+ URLEncoder.encode(personCode, "UTF-8")).openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));
			Map<String, Object> data = (Map<String, Object>) response
					.get("data");
			List<Object> mediaLinks = (ArrayList<Object>) data
					.get("mediaLinks");
			List<Map<String, Object>> filmography = (ArrayList<Map<String, Object>>) data
					.get("filmography");
			if (response == null) {
				System.out.println("The Responce is null !!! ");
			} else {

				if (data.get("id") == null) {
					actorsInfoList.add("no id available");
				} else {
					actorsInfoList.add((String) data.get("id"));
				}
				if (data.get("title") == null) {
					actorsInfoList.add("no title available");
				} else {
					actorsInfoList.add((String) data.get("title"));
				}
				if (data.get("image") == null) {
					actorsInfoList.add("no image available");
				} else {
					actorsInfoList.add((String) data.get("image"));
				}
				if (data.get("description") == null) {
					actorsInfoList.add("no description available");
				} else {
					actorsInfoList.add((String) data.get("description"));
				}
				tmdbId = getTMDBId(actorsInfoList.getFirst().toString());

				for (int i = 0; i < tmdbId.size(); i++) {
					actorsInfoList.add(tmdbId.get(i).toString());
				}

				tmdbDOB = getDOBInfo(tmdbId.get(0));

				for (int i = 0; i < tmdbDOB.size(); i++) {
					actorsInfoList.add(tmdbDOB.get(i).toString());
				}

			}
		} catch (Exception e) {

			System.out.println(e.getMessage().toString() + " error ");
		}

		return new PersonDetailModel(actorsInfoList);
	}
	
	/*
	 * 	This function provides the list of movies depending upon the Genre type passed as input parameter.
	 */
	private String personResource(String name) {
		String actorCode = "";
		try {
			InputStream input = new URL(
					"http://imdb.wemakesites.net/api/search?q="
							+ URLEncoder.encode(name, "UTF-8")).openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));
			if (response == null) {
				System.out.println("Response is null!!");
			} else {
				Map<String, Object> data = (Map<String, Object>) response
						.get("data");
				Map<String, Object> results = (Map<String, Object>) data
						.get("results");
				List<Map<String, Object>> names = (ArrayList<Map<String, Object>>) results
						.get("names");
				actorCode = names.get(0).get("id").toString();
			}

		} catch (Exception e) {

			System.out.println(e.getMessage().toString() + " error ");
		}

		return actorCode.toString();
	}

	/*
	 * 	This function provides the list of movies depending upon the Genre type passed as input parameter.
	 */
	private List<String> getTMDBId(String imdbId) {
		List<String> tmdbData = new ArrayList<String>();
		try {
			URL url = new URL(
					"https://api.themoviedb.org/3/find/"
							+ imdbId
							+ "?external_source=imdb_id&api_key=3eaf57ed7c6daae4f7ef9c460134ac0f");
			if (url == null) {
				System.out.println("url returned null");
				System.out.println("Url is  null!!");
				throw new NullPointerException();
			}
			System.out.println("Url is not null!!");
			InputStream input = url.openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));

			if (response == null) {
				System.out.println("Response is null!!");
				tmdbData.add("No ID Found");
				tmdbData.add("No Image");
				tmdbData.add("No Image");
				tmdbData.add("No Image");
			} else {

				List<Map<String, Object>> personResult = (ArrayList<Map<String, Object>>) response.get("person_results");
				if (personResult.get(0).get("id").toString() == null) {
					tmdbData.add("No ID Found");
				} else {
					tmdbData.add(personResult.get(0).get("id").toString());
				}
				List<Map<String, Object>> moviesResult = (ArrayList<Map<String, Object>>) personResult.get(0).get("known_for");

				if (moviesResult == null) {
					tmdbData.add("No Image");
				} else {
					for (int i = 0; i < 3; i++) {
						tmdbData.add("http://image.tmdb.org/t/p/w300"
								+ moviesResult.get(i).get("poster_path")
										.toString());
					}
				}
			}

		} catch (Exception e) {
			tmdbData.add("No ID Found");
			tmdbData.add("No Image");
			tmdbData.add("No Image");
			tmdbData.add("No Image");
			System.out.println(e.getMessage().toString()
					+ "Error in get TMDBID service");
		}
		return tmdbData;
	}

	/*
	 *  This function get the DOB and poster of writers, directors and actors when tmdbId is passed as input parameter.
	 */
	private List<String> getDOBInfo(String tmdbId) {
		List<String> tmdbDOBData = new ArrayList<String>();
		tmdbDOBData.clear();
		try {
			InputStream input = new URL("http://api.themoviedb.org/3/person/"
					+ tmdbId + "?api_key=3eaf57ed7c6daae4f7ef9c460134ac0f")
					.openStream();

			Map<String, Object> response = toMapObject(IOUtils.toString(input,
					"UTF-8"));

			if (response == null) {
				System.out.println("Response is null!!");
				tmdbDOBData.add("NO DOB DATA");
				tmdbDOBData.add("No Popularity Data Available");
				tmdbDOBData.add("No Place of birth mention");
			} else {

				if (response.get("birthday").toString() == null) {
					tmdbDOBData.add("NO DOB DATA");
				} else {
					tmdbDOBData.add(response.get("birthday").toString());
				}
				if (response.get("popularity").toString() == null) {
					tmdbDOBData.add("No Popularity Data Available");
				} else {
					tmdbDOBData.add(response.get("popularity").toString());
				}
				if ((response.get("place_of_birth").toString()) == null) {
					tmdbDOBData.add("No Place of birth mention");
				} else {
					tmdbDOBData.add(response.get("place_of_birth").toString());
				}

			}

		} catch (Exception e) {
			tmdbDOBData.add("NO DOB DATA");
			tmdbDOBData.add("No Popularity Data Available");
			tmdbDOBData.add("No Place of birth mention");
			System.out.println(e.getMessage().toString()
					+ "Error in get TMDBID date of birth service");
		}

		return tmdbDOBData;
	}
	
	/*
	 * 	This function is used to read the JSON response.
	 */
	public static Map<String, Object> toMapObject(String data) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(data,
					new TypeReference<Map<String, Object>>() {
					});
		} catch (Exception ex) {
			System.err
					.println("cannot convet to map<String, Object> : " + data);
			System.err.println(ex.getMessage());
		}

		return map;
	}
	
	/*
	 * 	This function is used to remove white-spaces and special characters.
	 */
	public static String toTrim(String name) {
		name = name.replace(" ", "");
		System.out.println(name.indexOf("("));
		if (name.contains("(")) {
			name = name.substring(0, name.indexOf("("));
		}
		if (name.contains(")")) {
			name = name.replace(")", "");
		}
		System.out.println(name);
		return name;
	}
	
	/*
	 * 	This is small lookup table for IMDB genre types.
	 */
	public enum GenreList {
		Action(28), Adventure(12), Animation(16), Comedy(35), Crime(80), Documentary(
				99), Drama(18), Family(10751), Fantasy(14), Foreign(10769), History(
				36), Horror(27), Music(10402), Mystery(9648), Romance(10749), SciFi(
				878), TVMovie(10770), Thriller(53), War(10752), Western(37);

		private int value;

		private GenreList(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/*
	 * 	This function return's the genre code when genre type is passed as input parameter.
	 */
	public static String getGenreCode(String genreName) {
		int result = 0;
		genreName = genreName.replace(" ", "");
		genreName = genreName.replace("-", "");
		switch (genreName) {
		case "Action":
			result = GenreList.Action.value;
			break;
		case "Adventure":
			result = GenreList.Adventure.value;
			break;
		case "Animation":
			result = GenreList.Animation.value;
			break;
		case "Comedy":
			result = GenreList.Comedy.value;
			break;
		case "Crime":
			result = GenreList.Crime.value;
			break;
		case "Documentary":
			result = GenreList.Documentary.value;
			break;
		case "Drama":
			result = GenreList.Drama.value;
			break;
		case "Family":
			result = GenreList.Family.value;
			break;
		case "Fantasy":
			result = GenreList.Fantasy.value;
			break;
		case "Foreign":
			result = GenreList.Foreign.value;
			break;
		case "History":
			result = GenreList.History.value;
			break;
		case "Horror":
			result = GenreList.Horror.value;
			break;
		case "Music":
			result = GenreList.Music.value;
			break;
		case "Mystery":
			result = GenreList.Mystery.value;
			break;
		case "Romance":
			result = GenreList.Romance.value;
			break;
		case "SciFi":
			result = GenreList.SciFi.value;
			break;
		case "RealityTV":
			result = GenreList.TVMovie.value;
			break;
		case "Thriller":
			result = GenreList.Thriller.value;
			break;
		case "War":
			result = GenreList.War.value;
			break;
		case "Western":
			result = GenreList.Western.value;
			break;
		default:
			result=0;
			break;
		}
		return String.valueOf(result);
	}

}
