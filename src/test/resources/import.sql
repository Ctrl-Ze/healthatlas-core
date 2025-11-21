INSERT INTO analytes (name, unit, normal_low, normal_high, description) VALUES
-- CBC (Complete Blood Count)
('HGB', 'g/dL', 12.0, 17.5, 'Hemoglobin'),
('RBC', '10^6/uL', 4.2, 5.9, 'Red Blood Cells'),
('WBC', '10^3/uL', 4.0, 11.0, 'White Blood Cells'),
('PLT', '10^3/uL', 150.0, 450.0, 'Platelets'),
('HCT', '%', 36.0, 50.0, 'Hematocrit'),
('MCV', 'fL', 80.0, 100.0, 'Mean Corpuscular Volume'),
('MCH', 'pg', 27.0, 33.0, 'Mean Corpuscular Hemoglobin'),
('MCHC', 'g/dL', 32.0, 36.0, 'Mean Corpuscular Hemoglobin Concentration'),
('RDW', '%', 11.0, 15.0, 'Red Cell Distribution Width'),
---️ Differential
('NEUT%', '%', 40.0, 75.0, 'Neutrophils Percentage'),
('LYMPH%', '%', 20.0, 45.0, 'Lymphocytes Percentage'),
('MONO%', '%', 2.0, 10.0, 'Monocytes Percentage'),
('EOS%', '%', 1.0, 6.0, 'Eosinophils Percentage'),
('BASO%', '%', 0.0, 2.0, 'Basophils Percentage'),

-- Basic Metabolic Panel (BMP)
('GLU', 'mg/dL', 70.0, 99.0, 'Glucose, fasting'),
('BUN', 'mg/dL', 7.0, 20.0, 'Blood Urea Nitrogen'),
('CREA', 'mg/dL', 0.6, 1.3, 'Creatinine'),
('NA', 'mmol/L', 135.0, 145.0, 'Sodium'),
('K', 'mmol/L', 3.5, 5.1, 'Potassium'),
('CL', 'mmol/L', 98.0, 107.0, 'Chloride'),
('CO2', 'mmol/L', 22.0, 29.0, 'Carbon Dioxide / Bicarbonate'),
('CA', 'mg/dL', 8.6, 10.2, 'Calcium'),

-- Liver Panel
('AST', 'U/L', 10.0, 40.0, 'Aspartate Aminotransferase'),
('ALT', 'U/L', 7.0, 56.0, 'Alanine Aminotransferase'),
('ALP', 'U/L', 44.0, 147.0, 'Alkaline Phosphatase'),
('BILI_TOT', 'mg/dL', 0.1, 1.2, 'Total Bilirubin'),
('BILI_DIR', 'mg/dL', 0.0, 0.4, 'Direct Bilirubin'),
('ALB', 'g/dL', 3.5, 5.0, 'Albumin'),
('TP', 'g/dL', 6.0, 8.3, 'Total Protein'),

-- Lipid Panel
('CHOL', 'mg/dL', 125.0, 200.0, 'Total Cholesterol'),
('HDL', 'mg/dL', 40.0, 60.0, 'HDL Cholesterol'),
('LDL', 'mg/dL', 0.0, 130.0, 'LDL Cholesterol'),
('TRIG', 'mg/dL', 0.0, 150.0, 'Triglycerides'),
('VLDL', 'mg/dL', 5.0, 40.0, 'Very Low Density Lipoprotein'),

-- Thyroid Panel
('TSH', 'uIU/mL', 0.4, 4.0, 'Thyroid Stimulating Hormone'),
('T4', 'ug/dL', 4.5, 11.2, 'Thyroxine Total'),
('FT4', 'ng/dL', 0.8, 1.8, 'Free Thyroxine'),
('T3', 'ng/dL', 80.0, 180.0, 'Triiodothyronine Total'),
('FT3', 'pg/mL', 2.3, 4.2, 'Free Triiodothyronine'),

-- Electrolytes & Minerals
('MG', 'mg/dL', 1.7, 2.2, 'Magnesium'),
('PHOS', 'mg/dL', 2.5, 4.5, 'Phosphate'),
('FE', 'ug/dL', 60.0, 170.0, 'Iron'),
('FERR', 'ng/mL', 20.0, 300.0, 'Ferritin'),
('TIBC', 'ug/dL', 240.0, 450.0, 'Total Iron Binding Capacity'),
('UIBC', 'ug/dL', 150.0, 375.0, 'Unsaturated Iron Binding Capacity'),
('TRANSFERRIN', 'mg/dL', 200.0, 360.0, 'Transferrin'),

-- Vitamins
('VITB12', 'pg/mL', 200.0, 900.0, 'Vitamin B12'),
('FOLATE', 'ng/mL', 3.0, 17.0, 'Folate'),
('VITD25', 'ng/mL', 30.0, 100.0, 'Vitamin D (25-Hydroxy)'),

-- Hormones
('CORTISOL', 'ug/dL', 5.0, 25.0, 'Cortisol (morning)'),
('TESTO_TOT', 'ng/dL', 300.0, 1000.0, 'Total Testosterone'),
('TESTO_FREE', 'pg/mL', 50.0, 210.0, 'Free Testosterone'),
('ESTRADIOL', 'pg/mL', 15.0, 350.0, 'Estradiol'),
('PROG', 'ng/mL', 0.1, 20.0, 'Progesterone'),

-- Inflammation Markers
('CRP', 'mg/L', 0.0, 5.0, 'C-Reactive Protein'),
('ESR', 'mm/hr', 0.0, 20.0, 'Erythrocyte Sedimentation Rate'),

-- Kidney Markers
('eGFR', 'mL/min/1.73m2', 60.0, 120.0, 'Estimated Glomerular Filtration Rate'),
('URIC', 'mg/dL', 3.5, 7.2, 'Uric Acid');