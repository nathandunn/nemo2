
update pattern_image 
set pattern_name = regexp_replace(pattern_name,'-%2B','-+');
update pattern_image 
set pattern_name = regexp_replace(pattern_name,'_%2B','_+');
