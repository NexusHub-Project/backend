# backend


NexusHub is a comprehensive League of Legends analytics platform designed for individual players who wish to improve their gameplay. It offers advanced statistical insights, champion matchup information, and gameplay tips based on live and historical data. The product is tailored toward everyday players who want to track their own performance, understand their strengths and weaknesses, and make data-driven decisions to play better.

Recommendations and analytics are generated through custom algorithms using Riot’s live match data and player history. OurScore and TeamScore are two metrics we calculate to assess individual and team performance across matches.

The product is built as a full-featured backend application using Spring Boot and MySQL, containerized with Docker for scalable deployment. In the near future, we plan to launch a frontend client and mobile companion app to improve accessibility.

The APIs we are using are:
• match (for match data)
• summoner (to fetch summoner info)
• league and champion-mastery (to calculate rankings and champion performance)

A core component of the project includes position-based champion rankings, matchup analytics, and champion-specific recommendations such as runes and builds.

We do not use the Tournaments API. This product is intended for public use, and we are requesting a production API key to sustain real-time data access at scale.