package amazon.restful;

public enum RequestMethod {
	GET{
		public String toString() {
			return "GET";
		};
	},
	POST{
		public String toString() {
			return "POST";
		};
	},
	PUT{
		public String toString() {
			return "PUT";
		};
	},
	DELETE{
		public String toString() {
			return "DELETE";
		};
	}
}
