# ------------------------------------- #
# Run Extractor 						#
# ------------------------------------- #
#!/bin/bash
java -Xms512m -Xmx4096m -jar /usr/local/batch/dl-batch/lib/dl-batch.jar -n ExtractorTitlesPerVenue &
MyPID=$!
echo $MyPID