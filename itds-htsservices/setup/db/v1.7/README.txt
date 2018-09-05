====================================
   HOW TO USE THE SNAPBACK SCRIPT
====================================

The snapback script is named final_snapback_CLEANSED_201807260134.sql

The first 3 lines have been altered as a safety precaution, so that they read literally ...

   USE YOUR_DATABASE_NAME
   GO
   SET IDENTITY_INSERT SCHEMA_EXAMPLE.LOOKUP_TABLE_EXAMPLE ON

... which is a safeguard against the file ever being executed prematurely.

Execute the following SQL statements:

   SELECT COUNT(ID) FROM YOUR_DATABASE_NAME.SCHEMA_EXAMPLE.LOOKUP_TABLE_EXAMPLE
   DELETE FROM YOUR_DATABASE_NAME.SCHEMA_EXAMPLE.LOOKUP_TABLE_EXAMPLE
   SELECT COUNT(ID) FROM YOUR_DATABASE_NAME.SCHEMA_EXAMPLE.LOOKUP_TABLE_EXAMPLE
   
Confirm the count has dropped to zero.

Open and execute the final snapback script after first fixing the first 3 lines (as mentioned above).

